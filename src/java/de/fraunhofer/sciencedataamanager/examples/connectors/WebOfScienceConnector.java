/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.examples.connectors;

import de.fraunhofer.sciencedataamanager.connectors.webofscience.*;
import de.fraunhofer.sciencedataamanager.domain.*;
import de.fraunhofer.sciencedataamanager.interfaces.ICloudPaperConnector;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author Moritz Mars
 */
public class WebOfScienceConnector implements ICloudPaperConnector {

    private ApplicationConfiguration applicationConfiguration;
    int progress = 0;
    int itemTreshhold = 300;

    public WebOfScienceConnector(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    public SearchDefinitonExecution getCloudPapers(SearchDefinition searchDefiniton) throws Exception {

        SearchDefinitonExecution searchDefinitonExecution = new SearchDefinitonExecution();
        searchDefinitonExecution.setStartExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));

        String query = "TS=(";
        for (SearchTerm searchTerm : searchDefiniton.getSearchTerms()) {
            query += searchTerm.getTerm();
            if (searchTerm.getOperation() != null) {
                query = query + " " + searchTerm.getOperation() + " ";
            }
        }
        query += ")";
        searchDefinitonExecution.setQuery(query);
        
        WOKMWSAuthenticateService WOKMWSAuthenticateService = new WOKMWSAuthenticateService();
        WOKMWSAuthenticate WOKMWSAuthenticate = WOKMWSAuthenticateService.getWOKMWSAuthenticatePort();

        String sessionIdentifier = WOKMWSAuthenticate.authenticate();

        WokSearchLiteService WokSearchService = new WokSearchLiteService();
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
        for (int startPage = 1; startPage < itemTreshhold; startPage = startPage + 100) {
            RetrieveParameters retrieveParameters = new RetrieveParameters();
            retrieveParameters.setCount(100);
            retrieveParameters.setFirstRecord(startPage);
            SearchResults results = wokSearch.search(queryParameters, retrieveParameters);
            searchDefinitonExecution.setTotalItems(results.getRecordsFound());

            for (LiteRecord currentRecord : results.getRecords()) {
                ScientificPaperMetaInformation scientificPaperMetaInformation = new ScientificPaperMetaInformation();

                try {
                    scientificPaperMetaInformation.setIdentifier_1(currentRecord.getUid());
                    if (!((List<LabelValuesPair>) currentRecord.getTitle()).isEmpty()) {
                        scientificPaperMetaInformation.setTitle(((List<LabelValuesPair>) currentRecord.getTitle()).get(0).getValue().get(0));
                    }
                    if (!((List<LabelValuesPair>) currentRecord.getSource()).isEmpty()) {
                        for (LabelValuesPair currentLabeValuePair : currentRecord.getSource()) {
                            switch (currentLabeValuePair.getLabel()) {
                                case "Issue":
                                    if (!currentLabeValuePair.getValue().isEmpty())
                                    scientificPaperMetaInformation.setScrIdentifier_1(currentLabeValuePair.getValue().get(0));
                                    break;
                                case "Pages":
                                    if (!currentLabeValuePair.getValue().isEmpty()) {
                                        String[] parts = currentLabeValuePair.getValue().get(0).split("-");

                                        if (parts.length == 2) {
                                            scientificPaperMetaInformation.setSrcStartPage(parts[0]);
                                            scientificPaperMetaInformation.setSrcEndPage(parts[1]);
                                        }
                                    }
                                    break;
                                case "Published.BiblioDate":
                                    if (!currentLabeValuePair.getValue().isEmpty())
                                    System.out.println(currentLabeValuePair.getValue().get(0));

                                    //scientificPaperMetaInformation.setScrIdentifier_1(currentLabeValuePair.getValue().get(0));
                                    break;
                                case "Published.BiblioYear":
                                    if (!currentLabeValuePair.getValue().isEmpty())
                                    //scientificPaperMetaInformation.setScrIdentifier_1(currentLabeValuePair.getValue().get(0));
                                    break;
                                case "SourceTitle":
                                    if (!currentLabeValuePair.getValue().isEmpty())
                                    scientificPaperMetaInformation.setSrcTitle(currentLabeValuePair.getValue().get(0));
                                    break;
                                case "Volume":
                                    if (!currentLabeValuePair.getValue().isEmpty())
                                    scientificPaperMetaInformation.setSrcVolume(Integer.parseInt(currentLabeValuePair.getValue().get(0)));
                                    break;
                            }
                        }
                    }

                    if (!((List<LabelValuesPair>) currentRecord.getAuthors()).isEmpty()) {
                        List<String> authors = ((List<LabelValuesPair>) currentRecord.getAuthors()).get(0).getValue();
                        for (String currentAuthor : authors) {
                            String[] parts = currentAuthor.split(",");
                            if (parts.length == 2) {
                                ScientificPaperMetaInformationAuthors scientificPaperMetaInformationAuthors = new ScientificPaperMetaInformationAuthors();
                                scientificPaperMetaInformationAuthors.setGivenName(parts[0]);
                                scientificPaperMetaInformationAuthors.setSurName(parts[1]);
                                scientificPaperMetaInformation.getAuthors().add(scientificPaperMetaInformationAuthors);
                            }
                        }
                    }
                    searchDefinitonExecution.getScientificPaperMetaInformation().add(scientificPaperMetaInformation);
                    crawledItems++;
                    progress++;
                } catch (Exception ex) {
                    this.applicationConfiguration.getLoggingManager().logException(ex);
                    ScientificPaperMetaInformationParseException scientificPaperMetaInformationParseException = new ScientificPaperMetaInformationParseException();
                    scientificPaperMetaInformationParseException.setParseState("Failed");
                    scientificPaperMetaInformationParseException.setParseException(ex);
                    scientificPaperMetaInformationParseException.setScientificPaperMetaInformation(scientificPaperMetaInformation);
                    searchDefinitonExecution.getScientificPaperMetaInformationParseException().add(scientificPaperMetaInformationParseException);
                }
            }

        }
        searchDefinitonExecution.setCrawledItems(crawledItems);
        searchDefinitonExecution.setFinishedExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        return searchDefinitonExecution;
    }

    @Override
    public int getCurrentProgress() throws Exception {
        return (progress / itemTreshhold) * 100;
    }

}
