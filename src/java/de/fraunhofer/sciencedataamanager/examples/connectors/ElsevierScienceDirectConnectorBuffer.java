package de.fraunhofer.sciencedataamanager.examples.connectors;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.fraunhofer.sciencedataamanager.interfaces.*;
import de.fraunhofer.sciencedataamanager.domain.*;
import de.fraunhofer.sciencedataamanager.datamanager.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ElsevierScienceDirectConnectorBuffer implements ICloudPaperConnector {

    private ApplicationConfiguration applicationConfiguration; 
    public ElsevierScienceDirectConnectorBuffer(ApplicationConfiguration applicationConfiguration)
    {
       this.applicationConfiguration = applicationConfiguration; 
    }    

    int progress = 0;
    int itemTreshhold = 500;

    @Override
    public SearchDefinitonExecution getCloudPapers(SearchDefinition searchDefiniton) throws Exception {
        //IMappingDefinitonManager mappingDefinitonManager = new MappingDefinitonManager(); 
        //List<MappingDefinition> mappingDefinitons = mappingDefinitonManager.getMappingDefinitons(""); 
        SearchDefinitonExecution searchDefinitonExecution = new SearchDefinitonExecution();
        searchDefinitonExecution.setStartExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        LinkedList<ScientificPaperMetaInformation> scientificPaperMetaInformation = null;

        String query = "";
        int crawledItems = 0;
        int localItemsFound = 0;
        int newItemsFound = 0;

        for (SearchTerm searchTerm : searchDefiniton.getSearchTerms()) {
            query += searchTerm.getTerm();
            if (searchTerm.getOperation() != null) {
                query = query + "+" + searchTerm.getOperation() + "+";
            }
        }

        query = query.replaceAll(" ", "%20");

        scientificPaperMetaInformation = new LinkedList<ScientificPaperMetaInformation>();

        for (int startPage = 0; startPage < itemTreshhold; startPage = startPage + 25) {
            System.out.println("start page" + startPage);
            String uri = "https://api.elsevier.com/content/search/index:scidir?apiKey=db7751387e990e0e08b3bccb1b1c67ed&query=" + query + "&start=" + startPage + "&field=doi";
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");

            InputStream xml = connection.getInputStream();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            org.w3c.dom.Document doc = dBuilder.parse(xml);

            doc.getDocumentElement().normalize();

            NodeList nList2 = doc.getElementsByTagName("opensearch:totalResults");
            Node nNode2 = nList2.item(0);

            searchDefinitonExecution.setTotalItems(Integer.parseInt(nNode2.getTextContent()));
            searchDefinitonExecution.setQuery(query);
            searchDefinitonExecution.setRequestUrl(uri);
            Element eElement2 = (Element) nNode2;

            NodeList nList = doc.getElementsByTagName("entry");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                ScientificPaperMetaInformation cloudPaperResult = new ScientificPaperMetaInformation();
                try {
                    Node nNode = nList.item(temp);

                    System.out.println("\nCurrent Element :" + nNode.getNodeName());

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        //for (MappingDefinition mappingDefiniton : mappingDefinitons)
                        if (eElement.getElementsByTagName("prism:doi").item(0) == null) {
                            continue;
                        }

                        cloudPaperResult.setIdentifier_1(eElement.getElementsByTagName("prism:doi").item(0).getTextContent());

                            ScientificPaperMetaInformationDataManager scientificPaperMetaInformationDataManager = new ScientificPaperMetaInformationDataManager(this.applicationConfiguration);
                        ScientificPaperMetaInformation scientificPaperMetaInformationBuffer = scientificPaperMetaInformationDataManager.getScientificMetaInformationByID(cloudPaperResult);
                        if (scientificPaperMetaInformationBuffer != null) {
                            localItemsFound++;
                            scientificPaperMetaInformation.add(scientificPaperMetaInformationBuffer);
                        } else {
                            String currentDOI = cloudPaperResult.getIdentifier_1().replace("DOI:", "");;
                            String queryuriItemDetailByDOI = "DOI(" + currentDOI + ")";
                            String uriItemDetailByDOI = "https://api.elsevier.com/content/search/index:scidir?apiKey=db7751387e990e0e08b3bccb1b1c67ed&query=" + queryuriItemDetailByDOI;
                            URL urlItemDetailByDOI = new URL(uriItemDetailByDOI);
                            HttpURLConnection connectionItemDetailByDOI = (HttpURLConnection) urlItemDetailByDOI.openConnection();
                            connectionItemDetailByDOI.setRequestMethod("GET");
                            connectionItemDetailByDOI.setRequestProperty("Accept", "application/xml");

                            InputStream xmlItemDetailByDOI = connectionItemDetailByDOI.getInputStream();

                            DocumentBuilderFactory dbFactoryItemDetailByDOI = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilderItemDetailByDOI = dbFactoryItemDetailByDOI.newDocumentBuilder();

                            org.w3c.dom.Document docItemDetailByDOI = dBuilderItemDetailByDOI.parse(xmlItemDetailByDOI);

                            docItemDetailByDOI.getDocumentElement().normalize();

                            NodeList nListItemDetailByDOI = docItemDetailByDOI.getElementsByTagName("entry");

                            for (int tempItemDetailByDOI = 0; tempItemDetailByDOI < nListItemDetailByDOI.getLength(); tempItemDetailByDOI++) {

                                Node nNodeItemDetailByDOI = nListItemDetailByDOI.item(tempItemDetailByDOI);

                                if (nNodeItemDetailByDOI.getNodeType() == Node.ELEMENT_NODE) {
                                    //for (MappingDefinition mappingDefiniton : mappingDefinitons)
                                    //{

                                    Element eElementItemDetailByDOI = (Element) nNodeItemDetailByDOI;
                                    Calendar calendar = Calendar.getInstance();
                                    java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

                                    ScientificPaperMetaInformationAuthors creator = new ScientificPaperMetaInformationAuthors();

                                    if (eElementItemDetailByDOI.getElementsByTagName("prism:coverDate").item(0) != null) {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        Date parsedDate = dateFormat.parse(eElementItemDetailByDOI.getElementsByTagName("prism:coverDate").item(0).getTextContent());
                                        java.sql.Date date = new java.sql.Date(parsedDate.getTime());
                                        cloudPaperResult.setSrcPublicationDate(date);
                                    }

                                    cloudPaperResult.setTitle(eElementItemDetailByDOI.getElementsByTagName("dc:title").item(0).getTextContent());
                                    //cloudPaperResult.setCreator(eElementItemDetailByDOI.getElementsByTagName("dc:creator").item(0).getTextContent());

                                    if (eElementItemDetailByDOI.getElementsByTagName("dc:creator").item(0) != null) {
                                        String[] parts = eElementItemDetailByDOI.getElementsByTagName("dc:creator").item(0).getTextContent().split(",");
                                        if (parts.length == 2) {
                                            creator.setGivenName(parts[0]);
                                            creator.setSurName(parts[1]);
                                        }
                                    }

                                    cloudPaperResult
                                            .setScrPublisherName(eElementItemDetailByDOI.getElementsByTagName("prism:publicationName").item(0).getTextContent());
                                    //cloudPaperResult.setCreateDate(ourJavaTimestampObject);
                                    //if (eElementItemDetailByDOI.getElementsByTagName("dc:identifier").item(0) != null) {
                                    //     cloudPaperResult.setIdentifier_1(eElementItemDetailByDOI.getElementsByTagName("dc:identifier").item(0).getTextContent());
                                    //}
                                    if (eElementItemDetailByDOI.getElementsByTagName("eid").item(0) != null) {
                                        cloudPaperResult.setIdentifier_2(eElementItemDetailByDOI.getElementsByTagName("eid").item(0).getTextContent());
                                    }
                                    if (eElementItemDetailByDOI.getElementsByTagName("prism:issn").item(0) != null) {
                                        cloudPaperResult.setIdentifier_3(eElementItemDetailByDOI.getElementsByTagName("prism:issn").item(0).getTextContent());
                                    }
                                    if (eElementItemDetailByDOI.getElementsByTagName("pii").item(0) != null) {
                                        cloudPaperResult.setIdentifier_4(eElementItemDetailByDOI.getElementsByTagName("pii").item(0).getTextContent());
                                    }

                                    if (eElementItemDetailByDOI.getElementsByTagName("prism:url").item(0) != null) {
                                        cloudPaperResult.setUrl_1(eElementItemDetailByDOI.getElementsByTagName("prism:url").item(0).getTextContent());
                                    }
                                    if (eElementItemDetailByDOI.getElementsByTagName("prism:teaser").item(0) != null) {
                                        cloudPaperResult.setText_1(eElementItemDetailByDOI.getElementsByTagName("prism:teaser").item(0).getTextContent());
                                    }

                                    if (eElementItemDetailByDOI.getElementsByTagName("prism:publicationName").item(0) != null) {
                                        cloudPaperResult.setScrPublisherName(eElementItemDetailByDOI.getElementsByTagName("prism:publicationName").item(0).getTextContent());
                                    }
                                    if (eElementItemDetailByDOI.getElementsByTagName("prism:issueName").item(0) != null) {
                                        cloudPaperResult.setSrcTitle(eElementItemDetailByDOI.getElementsByTagName("prism:issueName").item(0).getTextContent());
                                    }

                                    //if (eElementItemDetailByDOI.getElementsByTagName("rism:coverDate").item(0) != null) {
                                    //    cloudPaperResult.setSrcPublicationDate(eElementItemDetailByDOI.getElementsByTagName("prism:coverDisplayDate").item(0).getTextContent());
                                    //}
                                    if (eElementItemDetailByDOI.getElementsByTagName("prism:volume").item(0) != null) {
                                        if (eElementItemDetailByDOI.getElementsByTagName("prism:volume").item(0).getTextContent().matches("\\d+")) {
                                            cloudPaperResult.setSrcVolume(Integer.parseInt(eElementItemDetailByDOI.getElementsByTagName("prism:volume").item(0).getTextContent()));
                                        }
                                    }

                                    if (eElementItemDetailByDOI.getElementsByTagName("prism:startingPage").item(0) != null) {
                                        cloudPaperResult.setSrcStartPage(eElementItemDetailByDOI.getElementsByTagName("prism:startingPage").item(0).getTextContent());
                                    }

                                    if (eElementItemDetailByDOI.getElementsByTagName("prism:issueIdentifier").item(0) != null) {
                                        cloudPaperResult.setScrIdentifier_1(eElementItemDetailByDOI.getElementsByTagName("prism:issueIdentifier").item(0).getTextContent());
                                    }

                                    cloudPaperResult.setLocalizedTime(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));

                                    cloudPaperResult.getAuthors().add(creator);
                                    //}
                                    scientificPaperMetaInformation.add(cloudPaperResult);
                                    progress++;
                                    newItemsFound++;
                                    break;
                                }

                            }
                            xmlItemDetailByDOI.close();
                            connectionItemDetailByDOI.disconnect();
                        }

                        //}
                        crawledItems++;
                        progress++;
                    }
                } catch (Exception ex) {
                    this.applicationConfiguration.getLoggingManager().logException(ex);
                    ScientificPaperMetaInformationParseException scientificPaperMetaInformationParseException = new ScientificPaperMetaInformationParseException(); 
                    scientificPaperMetaInformationParseException.setParseState("Failed");
                    scientificPaperMetaInformationParseException.setParseException(ex);
                    scientificPaperMetaInformationParseException.setScientificPaperMetaInformation(cloudPaperResult);
                    searchDefinitonExecution.getScientificPaperMetaInformationParseException().add(scientificPaperMetaInformationParseException);
                    xml.close();
                    connection.disconnect();
                }
            }
            xml.close();
            connection.disconnect();

        }

        searchDefinitonExecution.setCrawledItems(crawledItems);
        searchDefinitonExecution.setNewItems(newItemsFound);
        searchDefinitonExecution.setLocalItemsFound(localItemsFound);

        searchDefinitonExecution.setScientificPaperMetaInformation(scientificPaperMetaInformation);

        searchDefinitonExecution.setFinishedExecutionTimestamp(
                new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        return searchDefinitonExecution;

    }

    public int getCurrentProgress() throws Exception {

        return (progress / itemTreshhold) * 100;
    }

}

