/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.examples.connectors;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.ScientificPaperMetaInformation;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import de.fraunhofer.sciencedataamanager.domain.SearchTerm;
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
        query+=")";
        
        WOKMWSAuthenticateService WOKMWSAuthenticateService = new WOKMWSAuthenticateService();
        WOKMWSAuthenticate WOKMWSAuthenticate = WOKMWSAuthenticateService.getWOKMWSAuthenticatePort();

        String sessionIdentifier = WOKMWSAuthenticate.authenticate();

        WokSearchLiteService WokSearchService = new WokSearchLiteService();
        WokSearchLite wokSearch = WokSearchService.getWokSearchLitePort();
        BindingProvider bp = (BindingProvider) wokSearch;

        //Map<String, Object> reqContext = bp.getRequestContext();
        //reqContext.get(MessageContext.HTTP_RESPONSE_HEADERS);
        //List<String> cookieHeaders = null;
        //Map<String, List<String>> responseHeaders = (Map<String, List<String>>) bp.getResponseContext().get("javax.xml.ws.http.response.headers");
        //cookieHeaders  = responseHeaders.get("Set-Cookie");
        //cookieHeaders.add("SID=\"" + sessionIdentifier + "\"");
        Map<String, Object> requestHeaders = new HashMap<String, Object>();
        requestHeaders.put("Cookie", Collections.singletonList("SID=\"" + sessionIdentifier + "\""));

        //WSBindingProvider bp = (WSBindingProvider) wokSearch;
        bp.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);

        //Cookie cookie = new Cookie("SID", sessionIdentifier);
        //org.apache.cxf.endpoint.Client client = org.apache.cxf.frontend.ClientProxy.getClient(wokSearch);
        //HTTPConduit http = (HTTPConduit) client.getConduit();
        //HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        //httpClientPolicy.setCookie(cookie.toString());
        //http.setClient(httpClientPolicy);
        //bp.setOutboundHeaders(Headers.create(new QName("SID"),sessionIdentifier));
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

            for (LiteRecord currentRecord : results.records) {
                ScientificPaperMetaInformation scientificPaperMetaInformation = new ScientificPaperMetaInformation();
                scientificPaperMetaInformation.setIdentifier_1(currentRecord.getUid());
                scientificPaperMetaInformation.setTitle(((List<LabelValuesPair>) currentRecord.getTitle()).get(0).getValue().get(0));
                searchDefinitonExecution.getScientificPaperMetaInformation().add(scientificPaperMetaInformation);
                crawledItems++;
                progress++;
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
