/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.sciencedataamanager.beans;

import de.fraunhofer.sciencedataamanager.business.DataExportExecutionManager;
import de.fraunhofer.sciencedataamanager.domain.ApplicationConfiguration;
import de.fraunhofer.sciencedataamanager.domain.ScientificPaperMetaInformation;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinition;
import de.fraunhofer.sciencedataamanager.domain.SearchDefinitonExecution;
import de.fraunhofer.sciencedataamanager.domain.SearchExecution;
import de.fraunhofer.sciencedataamanager.domain.SystemInstance;
import de.fraunhofer.sciencedataamanager.business.SearchExecutionManager;
import de.fraunhofer.sciencedataamanager.datamanager.ApplicationConfigurationDataManagerFactory;
import de.fraunhofer.sciencedataamanager.datamanager.DataExportInstanceDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.LoggingDatabaseManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitonExecutionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchExecutionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SystemInstanceDataManager;
import de.fraunhofer.sciencedataamanager.examples.connectors.ElsevierScienceDirectConnectorBuffer;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

/**
 *
 * @author Moritz Mars
 */
@ManagedBean(name = "search")
@ViewScoped
public class SearchManagement implements Serializable {

    private static final long serialVersionUID = -314414475508376585L;

    private boolean buttonRendered = false;
    private boolean progressBarEnabled = true;
    private int currentProgress = 0;
    private String selectedItem;
    private String selectedExportInstance;
    private String loadDataMessage;
    private Collection loadedSearchExecutionList;
    private Collection<SystemInstance> loadedSystemInstances;
    private Collection loadedDataExportInstances;
    private Collection loadedSelectedSystemInstances;

    public String getLoadDataMessage() {
        return loadDataMessage;
    }

    public Collection getLoadedDataExportInstances() {
        return loadedDataExportInstances;
    }

    public Collection getLoadedSelectedSystemInstances() {
        return loadedSelectedSystemInstances;
    }

    public Collection getLoadedSearchExecutionList() {
        return loadedSearchExecutionList;
    }

    public Collection<SystemInstance> getLoadedSystemInstances() {
        return loadedSystemInstances;
    }

    public String getSelectedExportInstance() {
        return selectedExportInstance;
    }

    public void setSelectedExportInstance(String selectedExportInstance) {
        this.selectedExportInstance = selectedExportInstance;
    }

    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();
    SearchExecutionManager searchExecutionManager = new SearchExecutionManager(applicationConfiguration);

    public boolean isProgressBarEnabled() {
        return progressBarEnabled;
    }

    public void setProgressBarEnabled(boolean progressBarEnabled) {
        this.progressBarEnabled = progressBarEnabled;
    }

    public SearchExecutionManager getSearchExecutionManager() {
        return searchExecutionManager;
    }

    public void setSearchExecutionManager(SearchExecutionManager searchExecutionManager) {
        this.searchExecutionManager = searchExecutionManager;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public int getCurrentProgressValue() {
        try {
            return searchExecutionManager.getProgressCurrentExecution();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public boolean isButtonRendered() {
        return buttonRendered;
    }

    public void setButtonRendered(boolean buttonRendered) {
        this.buttonRendered = buttonRendered;
    }

    public void loadData() {
        this.loadedSystemInstances = getSystemInstances();
        this.loadedDataExportInstances = getDataExportInstances();
        this.loadedSelectedSystemInstances = getSelectedSystemInstances();
        this.loadedSearchExecutionList = getAllSearchDefinitionExecutinsBySearchID();
    }

    public SearchDefinitonExecution getScientificPaperMetaInformationBySearchDefinition() {
        SearchDefinitonExecution searchDefinitonExecution = null;
        try {
            Collection<String> scientificPaperMetaInformation = new LinkedList();

            SearchDefinition searchDefinition = new SearchDefinition();
            if (selectedItem == null || "".equals(selectedItem)) {
                return null;
            }
            searchDefinition.setID(Integer.parseInt(selectedItem));

            SearchDefinitonExecutionDataManager searchDefinitonExecutionDataProvider = new SearchDefinitonExecutionDataManager(applicationConfiguration);
            searchDefinitonExecution = searchDefinitonExecutionDataProvider.getLastSearchDefinitionExecutionForSearchDefinition(searchDefinition);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return searchDefinitonExecution;
    }

    public Collection getSelectedSystemInstances() {
        Collection<String> selectedSystemInstances = new LinkedList();
        try {
            SearchExecutionDataManager searchExecutionDataProvider = new SearchExecutionDataManager(applicationConfiguration);
            SearchDefinition searchDefinition = new SearchDefinition();

            if (selectedItem == null || "".equals(selectedItem)) {
                return selectedSystemInstances;
            }
            searchDefinition.setID(Integer.parseInt(selectedItem));
            SearchExecution searchExecution = searchExecutionDataProvider.getSystemInstanceBySearchDefinition(searchDefinition);
            for (SystemInstance systemInstanceLoop : searchExecution.getSystemInstances()) {
                selectedSystemInstances.add(systemInstanceLoop.getID() + "");
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return selectedSystemInstances;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public Collection<SystemInstance> getSystemInstances() {
        Collection systemInstances = null;
        try {
            SystemInstanceDataManager systemInstanceDataProvider = new SystemInstanceDataManager(applicationConfiguration);
            systemInstances = systemInstanceDataProvider.getSystemInstances();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return systemInstances;
    }

    public Collection getSearchDefinitions() {
        Collection searchDefinitions = null;
        SearchDefinitionDataManager searchDefinitionDataProvider = new SearchDefinitionDataManager(applicationConfiguration);
        try {

            searchDefinitions = searchDefinitionDataProvider.getSearchDefinitions();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return searchDefinitions;
    }

    public Collection getDataExportInstances() {
        Collection dataExportInstances = null;

        DataExportInstanceDataManager dataExportInstanceDataManager = new DataExportInstanceDataManager(applicationConfiguration);
        try {

            dataExportInstances = dataExportInstanceDataManager.getDataExportInstances();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return dataExportInstances;
    }

    public void execute() {
        try {
            if (selectedItem == null || "".equals(selectedItem)) {
                return;
            }
            this.setCurrentProgress(0);
            searchExecutionManager.execute(Integer.parseInt(selectedItem));
            this.setCurrentProgress(0);
            
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Collection<SearchDefinitonExecution> getLastSearchDefinitionExecutinsBySearchID() {
        LinkedList<SearchDefinitonExecution> searchDefinitonExecutionList = new LinkedList<SearchDefinitonExecution>();
        try {

            if (selectedItem == null || "".equals(selectedItem)) {
                return searchDefinitonExecutionList;
            }
            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(Integer.parseInt(selectedItem));

            SearchDefinitonExecutionDataManager searchDefinitonExecutionDataProvider = new SearchDefinitonExecutionDataManager(applicationConfiguration);
            searchDefinitonExecutionList.add(searchDefinitonExecutionDataProvider.getLastSearchDefinitionExecutionForSearchDefinition(searchDefinition));

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return searchDefinitonExecutionList;
    }

    public Collection<SearchDefinitonExecution> getAllSearchDefinitionExecutinsBySearchID() {
        LinkedList<SearchDefinitonExecution> searchDefinitonExecutionList = new LinkedList<SearchDefinitonExecution>();
        try {

            if (selectedItem == null || "".equals(selectedItem)) {
                return searchDefinitonExecutionList;
            }
            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(Integer.parseInt(selectedItem));

            SearchDefinitonExecutionDataManager searchDefinitonExecutionDataProvider = new SearchDefinitonExecutionDataManager(applicationConfiguration);
            searchDefinitonExecutionList = searchDefinitonExecutionDataProvider.getSearchDefinitionExecutionForSearchDefinition(searchDefinition);

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return searchDefinitonExecutionList;
    }

    public Collection<ScientificPaperMetaInformation> getAllScientificMetaInformationBySearchID() {
        LinkedList<SearchDefinitonExecution> searchDefinitonExecutionList = new LinkedList<SearchDefinitonExecution>();
        LinkedList<ScientificPaperMetaInformation> scientificMetaInformationList = new LinkedList<ScientificPaperMetaInformation>();

        try {

            if (selectedItem == null || "".equals(selectedItem)) {
                return scientificMetaInformationList;
            }
            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(Integer.parseInt(selectedItem));

            SearchDefinitonExecutionDataManager searchDefinitonExecutionDataProvider = new SearchDefinitonExecutionDataManager(applicationConfiguration);
            searchDefinitonExecutionList = searchDefinitonExecutionDataProvider.getSearchDefinitionExecutionForSearchDefinition(searchDefinition);
            for (SearchDefinitonExecution searchDefinitionExecution : searchDefinitonExecutionList) {
                scientificMetaInformationList.addAll(searchDefinitionExecution.getScientificPaperMetaInformation());
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return scientificMetaInformationList;
    }

    public int getCurrentProgress() {
        try {
            return searchExecutionManager.getProgressCurrentExecution();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void loadTable() {
        this.loadedSelectedSystemInstances = getSelectedSystemInstances();
        this.loadedSearchExecutionList = getAllSearchDefinitionExecutinsBySearchID();
    }

    public void onLoad(ComponentSystemEvent event) {
        try {
            if (FacesContext.getCurrentInstance().isPostback()) {
                return;
            }
            this.loadDataMessage = "Data loading can take some time. Please be patient!";
            this.loadData();
            this.setProgressBarEnabled(false);
            this.setButtonRendered(true);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exportToExcel() {

        try {

            if (selectedItem == null || "".equals(selectedItem)) {
                return;
            }

            String excelExportDefaultFilename = "Excel_Export_" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date()) + ".xls";

            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(Integer.parseInt(selectedItem));
            LinkedList<SearchDefinitonExecution> searchDefinitonExecutionList = new LinkedList<SearchDefinitonExecution>();

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + excelExportDefaultFilename + "\"");

            SearchDefinitonExecutionDataManager searchDefinitonExecutionDataProvider = new SearchDefinitonExecutionDataManager(applicationConfiguration);
            searchDefinitonExecutionList = searchDefinitonExecutionDataProvider.getSearchDefinitionExecutionForSearchDefinition(searchDefinition);

            searchDefinitonExecutionDataProvider.exportToExcel(searchDefinitonExecutionList, externalContext.getResponseOutputStream());
            facesContext.responseComplete();

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void export() {

        try {

            if (selectedItem == null || "".equals(selectedItem)) {
                return;
            }
            if (selectedExportInstance == null || "".equals(selectedExportInstance)) {
                return;
            }

            String excelExportDefaultFilename = "Excel_Export_" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date()) + ".xls";

            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(Integer.parseInt(selectedItem));

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + excelExportDefaultFilename + "\"");

            DataExportExecutionManager dataExportExecutionManager = new DataExportExecutionManager(applicationConfiguration);
            dataExportExecutionManager.export(searchDefinition, Integer.parseInt(selectedExportInstance), externalContext.getResponseOutputStream());
            facesContext.responseComplete();

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }
}
