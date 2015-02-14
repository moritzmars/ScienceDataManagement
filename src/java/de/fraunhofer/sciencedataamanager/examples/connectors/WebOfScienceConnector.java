/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.examples.connectors;

import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import de.fraunhofer.sciencedataamanager.interfaces.ICloudPaperConnector;
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

    public WebOfScienceConnector(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    public SearchDefinitonExecution getCloudPapers(SearchDefinition cloudSearchPattern) throws Exception {

        WOKMWSAuthenticateService WOKMWSAuthenticateService = new WOKMWSAuthenticateService();
        WOKMWSAuthenticate WOKMWSAuthenticate = WOKMWSAuthenticateService.getWOKMWSAuthenticatePort();
        String sessionIdentifier = WOKMWSAuthenticate.authenticate();
        WokSearchService WokSearchService = new WokSearchService();
        WokSearch wokSearch = WokSearchService.getWokSearchPort();
        BindingProvider bp = (BindingProvider) wokSearch;
        Map<String, Object> reqContext = bp.getRequestContext();
        reqContext.get(MessageContext.HTTP_RESPONSE_HEADERS);
        
    List<String> cookieHeaders = null;
    Map<String, List<String>> responseHeaders = (Map<String, List<String>>) bp.getResponseContext().get("javax.xml.ws.http.response.headers");
    cookieHeaders  = responseHeaders.get("Set-Cookie");

        //Map<String, Object> requestHeaders = new HashMap<String, Object>();
    //requestHeaders.put("SID", sessionIdentifier);
    //WSBindingProvider bp = (WSBindingProvider) wokSearch;
    //bp.getRequestContext().put("Cookie", requestHeaders);
    //Cookie cookie = new Cookie("SID", sessionIdentifier);
    //org.apache.cxf.endpoint.Client client = org.apache.cxf.frontend.ClientProxy.getClient(wokSearch);
    //HTTPConduit http = (HTTPConduit) client.getConduit();
    //HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
    //httpClientPolicy.setCookie(cookie.toString());
    //http.setClient(httpClientPolicy);
    //bp.setOutboundHeaders(Headers.create(new QName("SID"),sessionIdentifier));
    QueryParameters queryParameters = new QueryParameters();

    queryParameters.setDatabaseId (

    "WOS");
    queryParameters.setUserQuery (

    "TS=(cadmium OR lead)");
    queryParameters.setQueryLanguage (
    "en");
        RetrieveParameters retrieveParameters = new RetrieveParameters();

    retrieveParameters.setCount (

    5);
    retrieveParameters.setFirstRecord (
    1);
        FullRecordSearchResults resultWokSearch = wokSearch.search(queryParameters, retrieveParameters);


return null;
    }

    @Override
        public int getCurrentProgress() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
