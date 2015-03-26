package de.fraunhofer.sciencedataamanager.examples.connectors;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.LinkedList;
import de.fraunhofer.sciencedataamanager.interfaces.*;
import de.fraunhofer.sciencedataamanager.domain.*;
import de.fraunhofer.sciencedataamanager.datamanager.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

public class ScienceDirectExpertSearch implements ICloudPaperConnector {

    private ApplicationConfiguration applicationConfiguration;

    public ScienceDirectExpertSearch(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    int progress = 0;
    int itemTreshhold = 50;

    @Override
    public SearchDefinitonExecution getCloudPapers(SearchDefinition searchDefiniton) throws Exception {
        //IMappingDefinitonManager mappingDefinitonManager = new MappingDefinitonManager(); 
        //List<MappingDefinition> mappingDefinitons = mappingDefinitonManager.getMappingDefinitons(""); 
        SearchDefinitonExecution searchDefinitonExecution = new SearchDefinitonExecution();
        searchDefinitonExecution.setStartExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        LinkedList<ScientificPaperMetaInformation> scientificPaperMetaInformationList = new LinkedList<ScientificPaperMetaInformation>();

        String query = "";
        int crawledItems = 0;
        int localItemsFound = 0;
        int newItemsFound = 0;
        itemTreshhold = searchDefiniton.getItemTreshhold();
      if(searchDefiniton.getSearchQueryMode() == "Expert")
      {
              query=searchDefiniton.getExpertQuery(); 
      }
      else
      {  
      for (SearchTerm searchTerm : searchDefiniton.getSearchTerms())
        {
            query += '"' + searchTerm.getTerm() + '"';
            if (searchTerm.getOperation() != null)
            {
                query =  query + "+" + searchTerm.getOperation() + "+";
            }
        }
       }
        query = query.replaceAll(" ", "%20");
        LinkedList<ScientificPaperMetaInformation> scientificPaperMetaInformationUidList = new LinkedList<ScientificPaperMetaInformation>();
        int count = 0; 
       boolean lastLoop = false;
       boolean firstRun = true; 
        for (int startPage = 0; startPage < itemTreshhold; startPage = startPage + 25)
        {
             if (startPage >= searchDefinitonExecution.getTotalItems()&&!firstRun)
            {
                count = searchDefinitonExecution.getTotalItems() - (startPage - 100);
                startPage = searchDefinitonExecution.getTotalItems() - count;
                lastLoop = true; 
            }
          String uri = "https://api.elsevier.com/content/search/index:scidir?apiKey=8fa7b0863932c14a436808d8893715f3&query=" + query + "&start=" + startPage + "&field=identifier";
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            connection.setConnectTimeout(50000);
            InputStream xml = connection.getInputStream();

            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = (Document) saxBuilder.build(xml);

            Element rootElement = document.getRootElement();

            searchDefinitonExecution.setTotalItems(Integer.parseInt(rootElement.getChild("totalResults", Namespace.getNamespace("http://a9.com/-/spec/opensearch/1.1/")).getText()));
            searchDefinitonExecution.setQuery(query);
            searchDefinitonExecution.setRequestUrl(uri);
            List<Element> entryList = rootElement.getChildren("entry", Namespace.getNamespace("http://www.w3.org/2005/Atom"));
            for (Element currentElemenet : entryList)
            {
                ScientificPaperMetaInformation scientificPaperMetaInformation = new ScientificPaperMetaInformation();
                Element identifierElement = currentElemenet.getChild("identifier", Namespace.getNamespace("http://purl.org/dc/elements/1.1/"));
                if(identifierElement==null)
                    continue;
                scientificPaperMetaInformation.setIdentifier_2(identifierElement.getText());
                scientificPaperMetaInformationUidList.add(scientificPaperMetaInformation);
            }
            xml.close();
            connection.disconnect();
             firstRun =false; 
            if(lastLoop)
              break;

        }

        for (ScientificPaperMetaInformation scientificPaperMetaInformation : scientificPaperMetaInformationUidList)
        {
            try
            {
                ScientificPaperMetaInformationDataManager scientificPaperMetaInformationDataManager = new ScientificPaperMetaInformationDataManager(this.applicationConfiguration);
                ScientificPaperMetaInformation scientificPaperMetaInformationBuffer = scientificPaperMetaInformationDataManager.getScientificMetaInformationByID(scientificPaperMetaInformation.getIdentifier_2(), "Identifier_2");
                //if(false)
                if (scientificPaperMetaInformationBuffer != null)
                {
                    localItemsFound++;
                    scientificPaperMetaInformationList.add(scientificPaperMetaInformationBuffer);
                }
                else
                {
                    String currentEID = scientificPaperMetaInformation.getIdentifier_2();
                    String uriItemDetailAbstract = "http://api.elsevier.com/content/abstract/scopus_id/" + currentEID + "?apiKey=8fa7b0863932c14a436808d8893715f3";
                    scientificPaperMetaInformation.setUrl_2(uriItemDetailAbstract);

                    URL urlItemDetailAbstract = new URL(uriItemDetailAbstract);

                    HttpURLConnection connectionItemDetailAbstract = (HttpURLConnection) urlItemDetailAbstract.openConnection();
                    connectionItemDetailAbstract.setRequestMethod("GET");
                    connectionItemDetailAbstract.setRequestProperty("Accept", "application/xml");
                    InputStream xmlItemDetailAbstract = connectionItemDetailAbstract.getInputStream();
                    SAXBuilder saxBuilder = new SAXBuilder();
                    Document documentAbstract = (Document) saxBuilder.build(xmlItemDetailAbstract);

                    Element rootElementAbstract = documentAbstract.getRootElement();

                    Element coreDataElement = this.getChildElement(rootElementAbstract, "coredata", "http://www.elsevier.com/xml/svapi/abstract/dtd");
                    if (coreDataElement != null)
                    {
                        
                       Element doiElement = this.getChildElement(coreDataElement, "doi", "http://prismstandard.org/namespaces/basic/2.0/");
                        if (doiElement != null)
                        {
                             scientificPaperMetaInformation.setIdentifier_1(doiElement.getText());
                        }

                        Element coverDateElement = this.getChildElement(coreDataElement, "coverDate", "http://prismstandard.org/namespaces/basic/2.0/");
                        if (coverDateElement != null)
                        {
                            Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(coverDateElement.getText());
                            java.sql.Date date = new java.sql.Date(parsedDate.getTime());
                            scientificPaperMetaInformation.setSrcPublicationDate(date);
                        }
                        Element titleElement = this.getChildElement(coreDataElement, "title", "http://purl.org/dc/elements/1.1/");
                        if (titleElement != null)
                        {
                            scientificPaperMetaInformation.setTitle(titleElement.getText());
                        }
                        Element publicationNameElement = this.getChildElement(coreDataElement, "publicationName", "http://prismstandard.org/namespaces/basic/2.0/");
                        if (publicationNameElement != null)
                        {
                            scientificPaperMetaInformation
                                    .setScrPublisherName(publicationNameElement.getText());
                        }

                        Element urlElement = this.getChildElement(coreDataElement, "url", "http://prismstandard.org/namespaces/basic/2.0/");
                        if (urlElement != null)
                        {
                            scientificPaperMetaInformation
                                    .setUrl_1(urlElement.getText());
                        }

                        Element volumeElement = this.getChildElement(coreDataElement, "volume", "http://prismstandard.org/namespaces/basic/2.0/");
                        if (volumeElement != null)
                        {
                            scientificPaperMetaInformation
                                    .setSrcVolume(Integer.parseInt(volumeElement.getText()));
                        }

                        //scientificPaperMetaInformation.setSrcTitle(coreDataElement.getChild("issueName", Namespace.getNamespace("http://prismstandard.org/namespaces/basic/2.0/")).getText());
                        Element startingPageElement = this.getChildElement(coreDataElement, "startingPage", "http://prismstandard.org/namespaces/basic/2.0/");
                        if (startingPageElement != null)
                        {
                            scientificPaperMetaInformation
                                    .setSrcStartPage(startingPageElement.getText());
                        }

                        Element endPageElement = this.getChildElement(coreDataElement, "endingPage", "http://prismstandard.org/namespaces/basic/2.0/");
                        if (endPageElement != null)
                        {
                            scientificPaperMetaInformation
                                    .setSrcEndPage(endPageElement.getText());
                        }

                        Element issueIdentifierElement = this.getChildElement(coreDataElement, "issueIdentifier", "http://prismstandard.org/namespaces/basic/2.0/");
                        if (issueIdentifierElement != null)
                        {
                            scientificPaperMetaInformation
                                    .setScrIdentifier_1(issueIdentifierElement.getText());
                        }

                        Element issnElement = this.getChildElement(coreDataElement, "issn", "http://prismstandard.org/namespaces/basic/2.0/");
                        if (issnElement != null)
                        {
                            scientificPaperMetaInformation
                                    .setSrcIISN(issnElement.getText());
                        }

                        Element descriptionElement = this.getChildElement(coreDataElement, "description", "http://purl.org/dc/elements/1.1/");
                        if (descriptionElement != null)
                        {
                            Element abstractElement = this.getChildElement(descriptionElement, "abstract", "http://www.elsevier.com/xml/svapi/abstract/dtd");
                            if (abstractElement != null)
                            {
                                Element abstractTextElement = this.getChildElement(abstractElement, "para", "http://www.elsevier.com/xml/ani/common");
                                if (abstractTextElement != null)
                                {
                                    scientificPaperMetaInformation
                                            .setText_1(abstractTextElement.getText());
                                }
                            }

                        }
                    }

                    Element authorsElement = this.getChildElement(rootElementAbstract, "authors", "http://www.elsevier.com/xml/svapi/abstract/dtd");
                    if (authorsElement != null)
                    {

                        List<Element> authorsElementList = this.getChildElements(authorsElement, "author", "http://www.elsevier.com/xml/svapi/abstract/dtd");
                        if (authorsElementList != null)
                        {
                            for (Element currentAuthor : authorsElementList)
                            {
                                ScientificPaperMetaInformationAuthors author = new ScientificPaperMetaInformationAuthors();

                                Element authorSurName = this.getChildElement(currentAuthor, "surname", "http://www.elsevier.com/xml/ani/common");
                                Element authorGivenName = this.getChildElement(currentAuthor, "given-name", "http://www.elsevier.com/xml/ani/common");
                                if (authorSurName != null)
                                {
                                    author.setSurName(authorSurName.getText());
                                }
                                if (authorGivenName != null)
                                {
                                    author.setGivenName(authorGivenName.getText());
                                }

                                scientificPaperMetaInformation.getAuthors().add(author);
                            }
                        }
                    }
                    List<Element> affiliationList = this.getChildElements(rootElementAbstract, "affiliation", "http://www.elsevier.com/xml/svapi/abstract/dtd");
                    if (affiliationList != null)
                    {
                        for (Element currentAffiliation : affiliationList)
                        {
                            Element affilnameElement = this.getChildElement(currentAffiliation, "affilname", "http://www.elsevier.com/xml/svapi/abstract/dtd");

                            ScientificPaperMetaInformationAffiliation scientificPaperMetaInformationAffiliation = new ScientificPaperMetaInformationAffiliation();
                            Attribute idAttribute = this.getAttribute(currentAffiliation, "id", "");
                            Attribute hrefAttribute = this.getAttribute(currentAffiliation, "href", "");

                            if (idAttribute != null)
                            {
                                scientificPaperMetaInformationAffiliation.setIdentifier_1(idAttribute.getValue());
                            }
                            if (hrefAttribute != null)
                            {
                                scientificPaperMetaInformationAffiliation.setUrl(hrefAttribute.getValue());
                            }
                            if (affilnameElement != null)
                            {
                                scientificPaperMetaInformationAffiliation.setName(affilnameElement.getText());
                            }
                            scientificPaperMetaInformation.getAffiliation().add(scientificPaperMetaInformationAffiliation);
                        }
                    }
                    xmlItemDetailAbstract.close();
                    connectionItemDetailAbstract.disconnect();

                    for (ScientificPaperMetaInformationAffiliation scientificPaperMetaInformationAffiliation : scientificPaperMetaInformation.getAffiliation())
                    {

                        if (scientificPaperMetaInformationAffiliation.getIdentifier_1() != null)
                        {
                            String uriItemDetailAffiliate = "http://api.elsevier.com/content/affiliation/affiliation_id/" + scientificPaperMetaInformationAffiliation.getIdentifier_1() + "?apiKey=8fa7b0863932c14a436808d8893715f3";

                            URL urlItemDetailAffiliate = new URL(uriItemDetailAffiliate);

                            HttpURLConnection connectionItemDetailAffiliate = (HttpURLConnection) urlItemDetailAffiliate.openConnection();
                            connectionItemDetailAffiliate.setRequestMethod("GET");
                            connectionItemDetailAffiliate.setRequestProperty("Accept", "application/xml");
                            InputStream xmlItemDetailAffiliate = connectionItemDetailAffiliate.getInputStream();

                            Document documentAffiliate = (Document) saxBuilder.build(xmlItemDetailAffiliate);

                            Element rootElementAffiliate = documentAffiliate.getRootElement();

                            Element coreDataAffiliateElement = this.getChildElement(rootElementAffiliate, "coredata", "");
                            if (coreDataAffiliateElement != null)
                            {
                                Element authorCountElement = this.getChildElement(coreDataAffiliateElement, "author", "");
                                Element documentCountElement = this.getChildElement(coreDataAffiliateElement, "document-count", "");
                                Element cityElement = this.getChildElement(rootElementAffiliate, "city", "");
                                Element countryElement = this.getChildElement(rootElementAffiliate, "country", "");

                                if (authorCountElement != null)
                                {
                                    scientificPaperMetaInformationAffiliation.setAuthor_Count(Integer.parseInt(authorCountElement.getText()));
                                }
                                if (documentCountElement != null)

                                {
                                    scientificPaperMetaInformationAffiliation.setDocument_Count(Integer.parseInt(documentCountElement.getText()));
                                }
                                if (cityElement != null)

                                {
                                    scientificPaperMetaInformationAffiliation.setCity(cityElement.getText());
                                }
                                if (countryElement != null)

                                {
                                    scientificPaperMetaInformationAffiliation.setCountry(countryElement.getText());
                                }
                            }
                            xmlItemDetailAffiliate.close();
                            connectionItemDetailAffiliate.disconnect();
                        }
                    }

                    progress++;
                    newItemsFound++;
                    scientificPaperMetaInformation.setLocalizedTime(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
                    scientificPaperMetaInformationList.add(scientificPaperMetaInformation);
                }
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
            crawledItems++;
        }

        searchDefinitonExecution.setCrawledItems(crawledItems);

        searchDefinitonExecution.setNewItems(newItemsFound);

        searchDefinitonExecution.setLocalItemsFound(localItemsFound);

        searchDefinitonExecution.setScientificPaperMetaInformation(scientificPaperMetaInformationList);

        searchDefinitonExecution.setFinishedExecutionTimestamp(
                new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        return searchDefinitonExecution;

    }

    public int getCurrentProgress() throws Exception {

        return (progress / itemTreshhold) * 100;
    }

    private Element getChildElement(Element element, String childName, String namespace) {

        Element childElement = element.getChild(childName, Namespace.getNamespace(namespace));
        if (childElement != null)
        {
            return childElement;
        }
        this.applicationConfiguration.getLoggingManager().log(String.format("Element %s not found!", childName), LogLevel.ERROR);
        return null;
    }

    private Attribute getAttribute(Element element, String attributeName, String namespace) {

        Attribute attribute = element.getAttribute(attributeName, Namespace.getNamespace(namespace));
        if (attribute != null)
        {
            return attribute;
        }
        this.applicationConfiguration.getLoggingManager().log(String.format("Attribute %s not found!", attributeName), LogLevel.ERROR);
        return null;
    }

    private List<Element> getChildElements(Element element, String childName, String namespace) {

        List<Element> childElement = element.getChildren(childName, Namespace.getNamespace(namespace));
        if (!childElement.isEmpty())
        {
            return childElement;
        }
        this.applicationConfiguration.getLoggingManager().log(String.format("Child elements %s not found!", childName), LogLevel.ERROR);
        return null;
    }

}
