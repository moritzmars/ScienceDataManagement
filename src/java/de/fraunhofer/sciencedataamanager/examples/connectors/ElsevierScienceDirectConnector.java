package de.fraunhofer.sciencedataamanager.connectors;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.LinkedList;
import java.text.SimpleDateFormat;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import de.fraunhofer.sciencedataamanager.interfaces.*;
import de.fraunhofer.sciencedataamanager.domain.*;
import java.util.Date;

public class ElsevierScienceDirectConnector implements ICloudPaperConnector {

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
            String uri = "https://api.elsevier.com/content/search/index:scidir?apiKey=db7751387e990e0e08b3bccb1b1c67ed&query=" + query + "&start=" + startPage;
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

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						//for (MappingDefinition mappingDefiniton : mappingDefinitons)
                    //{

                    Element eElement = (Element) nNode;
                    Calendar calendar = Calendar.getInstance();
                    java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

                    if (eElement.getElementsByTagName("dc:creator").item(0) == null) {
                        continue;
                    }

                    ScientificPaperMetaInformationAuthors creator = new ScientificPaperMetaInformationAuthors();

                    if (eElement.getElementsByTagName("prism:coverDate").item(0) != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date parsedDate = dateFormat.parse(eElement.getElementsByTagName("prism:coverDate").item(0).getTextContent());
                        java.sql.Date date = new java.sql.Date(parsedDate.getTime());
                        cloudPaperResult.setSrcPublicationDate(date);
                    }

                    cloudPaperResult.setTitle(eElement.getElementsByTagName("dc:title").item(0).getTextContent());
						//cloudPaperResult.setCreator(eElement.getElementsByTagName("dc:creator").item(0).getTextContent());

                    if (eElement.getElementsByTagName("dc:creator").item(0) != null) {
                        String[] parts = eElement.getElementsByTagName("dc:creator").item(0).getTextContent().split(",");
                        if (parts.length == 2) {
                            creator.setGivenName(parts[0]);
                            creator.setSurName(parts[1]);
                        }
                    }

                    cloudPaperResult
                            .setScrPublisherName(eElement.getElementsByTagName("prism:publicationName").item(0).getTextContent());
                    //cloudPaperResult.setCreateDate(ourJavaTimestampObject);
                    if (eElement.getElementsByTagName("dc:identifier").item(0) != null) {
                        cloudPaperResult.setIdentifier_1(eElement.getElementsByTagName("dc:identifier").item(0).getTextContent());
                    }
                    if (eElement.getElementsByTagName("eid").item(0) != null) {
                        cloudPaperResult.setIdentifier_2(eElement.getElementsByTagName("eid").item(0).getTextContent());
                    }
                    if (eElement.getElementsByTagName("prism:issn").item(0) != null) {
                        cloudPaperResult.setIdentifier_3(eElement.getElementsByTagName("prism:issn").item(0).getTextContent());
                    }
                    if (eElement.getElementsByTagName("pii").item(0) != null) {
                        cloudPaperResult.setIdentifier_4(eElement.getElementsByTagName("pii").item(0).getTextContent());
                    }
                    scientificPaperMetaInformation.add(cloudPaperResult);
                    if (eElement.getElementsByTagName("prism:url").item(0) != null) {
                        cloudPaperResult.setUrl_1(eElement.getElementsByTagName("prism:url").item(0).getTextContent());
                    }
                    if (eElement.getElementsByTagName("prism:teaser").item(0) != null) {
                        cloudPaperResult.setText_1(eElement.getElementsByTagName("prism:teaser").item(0).getTextContent());
                    }

                    if (eElement.getElementsByTagName("prism:publicationName").item(0) != null) {
                        cloudPaperResult.setScrPublisherName(eElement.getElementsByTagName("prism:publicationName").item(0).getTextContent());
                    }
                    if (eElement.getElementsByTagName("prism:issueName").item(0) != null) {
                        cloudPaperResult.setSrcTitle(eElement.getElementsByTagName("prism:issueName").item(0).getTextContent());
                    }

                    //if (eElement.getElementsByTagName("rism:coverDate").item(0) != null) {
                    //    cloudPaperResult.setSrcPublicationDate(eElement.getElementsByTagName("prism:coverDisplayDate").item(0).getTextContent());
                    //}

                    if (eElement.getElementsByTagName("prism:volume").item(0) != null) {
                        if (eElement.getElementsByTagName("prism:volume").item(0).getTextContent().matches("\\d+")) {
                            cloudPaperResult.setSrcVolume(Integer.parseInt(eElement.getElementsByTagName("prism:volume").item(0).getTextContent()));
                        }
                    }

                    if (eElement.getElementsByTagName("prism:startingPage").item(0) != null) {
                        cloudPaperResult.setSrcStartPage(eElement.getElementsByTagName("prism:startingPage").item(0).getTextContent());
                    }

                    if (eElement.getElementsByTagName("prism:issueIdentifier").item(0) != null) {
                        cloudPaperResult.setScrIdentifier_1(eElement.getElementsByTagName("prism:issueIdentifier").item(0).getTextContent());
                    }

                    cloudPaperResult.setLocalizedTime(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));

                    cloudPaperResult.getAuthors().add(creator);
                    //}
                    crawledItems++;
                    progress++;
                }
            }
            connection.disconnect();
        }
        searchDefinitonExecution.setCrawledItems(crawledItems);
        searchDefinitonExecution.setScientificPaperMetaInformation(scientificPaperMetaInformation);
        searchDefinitonExecution.setFinishedExecutionTimestamp(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        return searchDefinitonExecution;

    }

    public int getCurrentProgress() throws Exception {

        return (progress / itemTreshhold) * 100;
    }

}
