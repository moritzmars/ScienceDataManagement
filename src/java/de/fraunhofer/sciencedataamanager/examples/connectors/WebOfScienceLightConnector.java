/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.examples.connectors;

import de.fraunhofer.sciencedataamanager.connectors.webofscience.light.*;
import de.fraunhofer.sciencedataamanager.connectors.webofscience.authenticate.*;
import de.fraunhofer.sciencedataamanager.datamanager.ScientificPaperMetaInformationDataManager;
import de.fraunhofer.sciencedataamanager.domain.*;
import de.fraunhofer.sciencedataamanager.interfaces.ICloudPaperConnector;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author Moritz Mars
 */
public class WebOfScienceLightConnector implements ICloudPaperConnector {

    private ApplicationConfiguration applicationConfiguration;
    int progress = 0;
    int itemTreshhold = 50;

    public WebOfScienceLightConnector(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    public SearchDefinitonExecution getCloudPapers(SearchDefinition searchDefiniton) throws Exception {

        SearchDefinitonExecution searchDefinitonExecution = new SearchDefinitonExecution();
        searchDefinitonExecution.setStartExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));

        String query = "TS=(";
        for (SearchTerm searchTerm : searchDefiniton.getSearchTerms())
        {
            query += searchTerm.getTerm();
            if (searchTerm.getOperation() != null)
            {
                query = query + " " + searchTerm.getOperation() + " ";
            }
        }
        query += ")";
        searchDefinitonExecution.setQuery(query);

        URL urlWokAuthenticate = new URL("http://search.webofknowledge.com/esti/wokmws/ws/WOKMWSAuthenticate?wsdl");

        URL urlWokSearchLite = new URL("http://search.webofknowledge.com/esti/wokmws/ws/WokSearchLite?wsdl");
        URL urlWokSearch = new URL("http://search.webofknowledge.com/esti/wokmws/ws/WokSearch?wsdl");

        searchDefinitonExecution.setRequestUrl(urlWokSearchLite.toString());

        WOKMWSAuthenticateService WOKMWSAuthenticateService = new WOKMWSAuthenticateService(urlWokAuthenticate);
        WOKMWSAuthenticate WOKMWSAuthenticate = WOKMWSAuthenticateService.getWOKMWSAuthenticatePort();

        String sessionIdentifier = WOKMWSAuthenticate.authenticate();

        WokSearchLiteService WokSearchService = new WokSearchLiteService(urlWokSearchLite);
        WokSearchLite wokSearch = WokSearchService.getWokSearchLitePort();

        BindingProvider bp = (BindingProvider) wokSearch;

        Map<String, Object> requestHeaders = new HashMap<String, Object>();
        requestHeaders.put("Cookie", Collections.singletonList("SID=\"" + sessionIdentifier + "\""));

        bp.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);

        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setDatabaseId("WOS");
        queryParameters.setUserQuery(query);
        queryParameters.setQueryLanguage("en");

        int crawledItems = 0;
        int localItemsFound = 0;
        int newItemsFound = 0;
        RetrieveParameters retrieveParameters = new RetrieveParameters();
        retrieveParameters.setCount(0);
        retrieveParameters.setFirstRecord(1);

        SearchResults results = wokSearch.search(queryParameters, retrieveParameters);

        searchDefinitonExecution.setTotalItems(results.getRecordsFound());
        //for (; startPage < itemTreshhold; )
        int startPage = 1;
        int count = 100;
        boolean lastLoop = false; 
        while (true)
        {
            
            if (startPage >= itemTreshhold)
            {
                count = itemTreshhold - (startPage - 100);
                startPage = itemTreshhold - count;
                lastLoop = true; 
            }

            if (startPage >= searchDefinitonExecution.getTotalItems())
            {
                count = searchDefinitonExecution.getTotalItems() - (startPage - 100);
                startPage = searchDefinitonExecution.getTotalItems() - count;
                lastLoop = true; 
            }
            retrieveParameters.setCount(count);
            retrieveParameters.setFirstRecord(startPage);
            results = wokSearch.retrieve(results.getQueryId(), retrieveParameters);

            for (LiteRecord currentRecord : results.getRecords())
            {
                ScientificPaperMetaInformationDataManager scientificPaperMetaInformationDataManager = new ScientificPaperMetaInformationDataManager(this.applicationConfiguration);
                ScientificPaperMetaInformation scientificPaperMetaInformationBuffer = scientificPaperMetaInformationDataManager.getScientificMetaInformationByID(currentRecord.getUid(), "Identifier_2");
                if (false)
                //if (scientificPaperMetaInformationBuffer != null) 
                {
                    localItemsFound++;
                    searchDefinitonExecution.getScientificPaperMetaInformation().add(scientificPaperMetaInformationBuffer);
                }
                else
                {

                    ScientificPaperMetaInformation scientificPaperMetaInformation = new ScientificPaperMetaInformation();
                    try
                    {
                        scientificPaperMetaInformation.setIdentifier_2(currentRecord.getUid());
                        if (!((List<LabelValuesPair>) currentRecord.getTitle()).isEmpty())
                        {
                            scientificPaperMetaInformation.setTitle(((List<LabelValuesPair>) currentRecord.getTitle()).get(0).getValue().get(0));
                        }

                        if (!((List<LabelValuesPair>) currentRecord.getSource()).isEmpty())
                        {
                            int monthNumber = 0;
                            int dayNumber = 1;
                            int yearNumber = 0;
                            for (LabelValuesPair currentLabeValuePair : currentRecord.getSource())
                            {

                                switch (currentLabeValuePair.getLabel())
                                {
                                    case "Issue":
                                        if (!currentLabeValuePair.getValue().isEmpty())
                                        {
                                            scientificPaperMetaInformation.setScrIdentifier_1(currentLabeValuePair.getValue().get(0));
                                        }
                                        break;
                                    case "Pages":
                                        if (!currentLabeValuePair.getValue().isEmpty())
                                        {
                                            String[] parts = currentLabeValuePair.getValue().get(0).split("-");

                                            if (parts.length == 2)
                                            {
                                                scientificPaperMetaInformation.setSrcStartPage(parts[0]);
                                                scientificPaperMetaInformation.setSrcEndPage(parts[1]);
                                            }
                                        }
                                        break;
                                    case "Published.BiblioDate":
                                        if (!currentLabeValuePair.getValue().isEmpty())
                                        {
                                            String[] parts = currentLabeValuePair.getValue().get(0).split(" ");

                                            if (parts.length >= 1)
                                            {

                                                switch (parts[0])
                                                {
                                                    case "SPR":
                                                        monthNumber = 3;
                                                        break;
                                                    case "SUM":
                                                        monthNumber = 6;
                                                        break;
                                                    case "FAL":
                                                        monthNumber = 9;
                                                        break;
                                                    case "WIN":
                                                        monthNumber = 12;
                                                        break;
                                                    default:
                                                        Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(parts[0]);
                                                        Calendar cal = Calendar.getInstance();
                                                        cal.setTime(date);
                                                        monthNumber = cal.get(Calendar.MONTH);
                                                        break;
                                                }

                                            }
                                            if (parts.length == 2)
                                            {
                                                dayNumber = Integer.parseInt(parts[1]);
                                            }

                                        }

                                        //scientificPaperMetaInformation.setScrIdentifier_1(currentLabeValuePair.getValue().get(0));
                                        break;
                                    case "Published.BiblioYear":
                                        if (!currentLabeValuePair.getValue().isEmpty()) //scientificPaperMetaInformation.setScrIdentifier_1(currentLabeValuePair.getValue().get(0));
                                        {
                                            yearNumber = Integer.parseInt(currentLabeValuePair.getValue().get(0));
                                            break;
                                        }
                                    case "SourceTitle":
                                        if (!currentLabeValuePair.getValue().isEmpty())
                                        {
                                            scientificPaperMetaInformation.setScrPublisherName(currentLabeValuePair.getValue().get(0));
                                        }
                                        break;
                                    case "Volume":
                                        if (!currentLabeValuePair.getValue().isEmpty())
                                        {
                                            scientificPaperMetaInformation.setSrcVolume(Integer.parseInt(currentLabeValuePair.getValue().get(0)));
                                        }
                                        break;
                                }

                            }

                            Calendar cal = Calendar.getInstance();
                            cal.set(yearNumber, monthNumber, dayNumber);
                            scientificPaperMetaInformation.setSrcPublicationDate(new java.sql.Date(cal.getTime().getTime()));
                        }

                        if (!((List<LabelValuesPair>) currentRecord.getOther()).isEmpty())
                        {
                            for (LabelValuesPair currentLabeValuePair : currentRecord.getOther())
                            {
                                switch (currentLabeValuePair.getLabel())
                                {
                                    case "Identifier.Doi":
                                        if (!currentLabeValuePair.getValue().isEmpty())
                                        {
                                            scientificPaperMetaInformation.setIdentifier_1(currentLabeValuePair.getValue().get(0));
                                        }
                                        break;
                                    case "Identifier.Ids":
                                        if (!currentLabeValuePair.getValue().isEmpty())
                                        {
                                            scientificPaperMetaInformation.setIdentifier_3(currentLabeValuePair.getValue().get(0));
                                        }
                                        break;
                                    case "Identifier.Issn":
                                        if (!currentLabeValuePair.getValue().isEmpty())
                                        {
                                            scientificPaperMetaInformation.setSrcIISN(currentLabeValuePair.getValue().get(0));
                                        }
                                        break;
                                    case "Identifer.Ibsn":
                                        if (!currentLabeValuePair.getValue().isEmpty())
                                        {
                                            scientificPaperMetaInformation.setSrcISBN(currentLabeValuePair.getValue().get(0));
                                        }
                                        break;

                                }
                            }
                        }

                        if (!((List<LabelValuesPair>) currentRecord.getAuthors()).isEmpty())
                        {
                            List<String> authors = ((List<LabelValuesPair>) currentRecord.getAuthors()).get(0).getValue();
                            for (String currentAuthor : authors)
                            {
                                String[] parts = currentAuthor.split(",");
                                if (parts.length == 2)
                                {
                                    ScientificPaperMetaInformationAuthors scientificPaperMetaInformationAuthors = new ScientificPaperMetaInformationAuthors();
                                    scientificPaperMetaInformationAuthors.setGivenName(parts[0]);
                                    scientificPaperMetaInformationAuthors.setSurName(parts[1]);
                                    scientificPaperMetaInformation.getAuthors().add(scientificPaperMetaInformationAuthors);
                                }
                            }
                        }
                        scientificPaperMetaInformation.setLocalizedTime(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));

                        searchDefinitonExecution.getScientificPaperMetaInformation().add(scientificPaperMetaInformation);
                        newItemsFound++;

                    }
                    catch (Exception ex)
                    {
                        this.applicationConfiguration.getLoggingManager().logException(ex);
                        ScientificPaperMetaInformationParseException scientificPaperMetaInformationParseException = new ScientificPaperMetaInformationParseException();
                        scientificPaperMetaInformationParseException.setParseState("Failed");
                        scientificPaperMetaInformationParseException.setParseException(ex);
                        scientificPaperMetaInformationParseException.setScientificPaperMetaInformation(scientificPaperMetaInformation);
                        searchDefinitonExecution.getScientificPaperMetaInformationParseException().add(scientificPaperMetaInformationParseException);
                    }
                }
                crawledItems++;
                progress++;
            }
            startPage = startPage + 100;
            if (lastLoop)
            {
                break;
            }

        }
        searchDefinitonExecution.setCrawledItems(crawledItems);
        searchDefinitonExecution.setLocalItemsFound(localItemsFound);
        searchDefinitonExecution.setNewItems(newItemsFound);
        searchDefinitonExecution.setFinishedExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        return searchDefinitonExecution;
    }

    @Override
    public int getCurrentProgress() throws Exception {
        return (progress / itemTreshhold) * 100;
    }

}
