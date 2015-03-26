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
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchDefinitonExecutionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SearchExecutionDataManager;
import de.fraunhofer.sciencedataamanager.datamanager.SystemInstanceDataManager;
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
 * This class provides logic and data for the search mangement site. 
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

    /**
     * Returns the load data message. 
     * @return the load data message. 
     */
    public String getLoadDataMessage() {
        return loadDataMessage;
    }

    /**
     * Returns the loaded data export instances. 
     * @return the loaded data export instances. 
     */
    public Collection getLoadedDataExportInstances() {
        return loadedDataExportInstances;
    }

    /**
     *Return the selected system instances.
     * @return the selected system instances. 
     */
    public Collection getLoadedSelectedSystemInstances() {
        return loadedSelectedSystemInstances;
    }

    /**
     * Returns the loaded search execution list. 
     * @return the loaded search execution list. 
     */
    public Collection getLoadedSearchExecutionList() {
        return loadedSearchExecutionList;
    }

    /**
     * Returns the loaded system instances. 
     * @return the loaded system instances. 
     */
    public Collection<SystemInstance> getLoadedSystemInstances() {
        return loadedSystemInstances;
    }

    /**
     * Returns the selected export instance. 
     * @return the selected export instance. 
     */
    public String getSelectedExportInstance() {
        return selectedExportInstance;
    }

    /**
     * Sets the selected export instance. 
     * @param selectedExportInstance the selected export instance. 
     */
    public void setSelectedExportInstance(String selectedExportInstance) {
        this.selectedExportInstance = selectedExportInstance;
    }

    /**
     * Returns the application configuration. 
     * @return the application configuration. 
     */
    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    /**
     * Sets the application configuration. 
     * @param the application configuration. 
     */
    public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    private ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDataManagerFactory.getApplicationConfigurationDataProvider(null).getApplicationConfiguration();
    SearchExecutionManager searchExecutionManager = new SearchExecutionManager(applicationConfiguration);

    /**
     * Returns the progress bar enabled attribute. 
     * @return the progress bar enabled attribute. 
     */
    public boolean isProgressBarEnabled() {
        return progressBarEnabled;
    }

    /**
     * Sets the progress bar enabled attribute. 
     * @param the progress bar enabled attribute. 
     */
    public void setProgressBarEnabled(boolean progressBarEnabled) {
        this.progressBarEnabled = progressBarEnabled;
    }

    /**
     * Returns the search execution manager. 
     * @return the search execution manager. 
     */
    public SearchExecutionManager getSearchExecutionManager() {
        return searchExecutionManager;
    }

    /**
     * Sets the search execution manager. 
     * @param searchExecutionManager the search execution manager. 
     */
    public void setSearchExecutionManager(SearchExecutionManager searchExecutionManager) {
        this.searchExecutionManager = searchExecutionManager;
    }

    /**
     * Sets the current progress.
     * @param currentProgress the current progress. 
     */
    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    /**
     * Returns the current progress values. 
     * @return the current progress value. 
     */
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

    /**
     * Returns the is button rendered flag. 
     * @return the is button rendered flar. 
     */
    public boolean isButtonRendered() {
        return buttonRendered;
    }

    /**
     * Sets the button rendered. 
     * @param buttonRendered the button rendered. 
     */
    public void setButtonRendered(boolean buttonRendered) {
        this.buttonRendered = buttonRendered;
    }

    /**
     * Loads the system instances, data export instances, selected system instances and all search definition execution. 
     */
    public void loadData() {
        this.loadedSystemInstances = getSystemInstances();
        this.loadedDataExportInstances = getDataExportInstances();
        this.loadedSelectedSystemInstances = getSelectedSystemInstances();
        this.loadedSearchExecutionList = getAllSearchDefinitionExecutinsBySearchID();
    }

    /**
     *Returns the scientific paper meta informations. 
     * @return the scientific paper meta information.
     */
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

    /**
     * Returns the selected system instances. 
     * @return the selected system instances. 
     */
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

    /**
     * Sets the selected item. 
     * @param selectedItem the selected item. 
     */
    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    /**
     * Returns the selected item. 
     * @return the selected item. 
     */
    public String getSelectedItem() {
        return selectedItem;
    }

    /**
     * Returns the system instances. 
     * @return the system instances. 
     */
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

    /**
     * Return the search definitions. 
     * @return the search definitions. 
     */
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

    /**
     * Returns the data export instances. 
     * @return the data export instances. 
     */
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

    /**
     * Executes the search algorithmus. 
     */
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
    
        /**
     * Executes the search algorithmus. 
     */
    public void executeOnlyInformation() {
        try {
            if (selectedItem == null || "".equals(selectedItem)) {
                return;
            }
            this.setCurrentProgress(0);
            searchExecutionManager.executeOnlyInformations(Integer.parseInt(selectedItem));
            this.setCurrentProgress(0);
            
            //FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns the last search definition execution. 
     * @return the last search definition execution. 
     */
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

    /**
     * Returns all search definition execution by search ID.
     * @return all search definition execution 
     */
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

    /**
     * Returns all scientific meta informations.
     * @return scientific meta informations. 
     */
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

    /**
     * Returns the current progress value. 
     * @return the current progress value. 
     */
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

    /**
     * Loads the selected system instances and all search definitions. 
     */
    public void loadTable() {
        this.loadedSelectedSystemInstances = getSelectedSystemInstances();
        this.loadedSearchExecutionList = getAllSearchDefinitionExecutinsBySearchID();
    }

    /**
     * The method is executed after the page is loaded. 
     * @param Informations about the page load event. 
     */
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

    /**
     * Export the data to excel.
     */
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

    /**
     * Executes the generic export algorithmus. 
     */
    public void export() {

        try {

            if (selectedItem == null || "".equals(selectedItem)) {
                return;
            }
            if (selectedExportInstance == null || "".equals(selectedExportInstance)) {
                return;
            }

          
            SearchDefinition searchDefinition = new SearchDefinition();
            searchDefinition.setID(Integer.parseInt(selectedItem));
        
            DataExportExecutionManager dataExportExecutionManager = new DataExportExecutionManager(applicationConfiguration);
            dataExportExecutionManager.export(searchDefinition, Integer.parseInt(selectedExportInstance), FacesContext.getCurrentInstance().getExternalContext());
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The following error occured: " + ex.toString()));
            this.applicationConfiguration.getLoggingManager().logException(ex);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }
}
