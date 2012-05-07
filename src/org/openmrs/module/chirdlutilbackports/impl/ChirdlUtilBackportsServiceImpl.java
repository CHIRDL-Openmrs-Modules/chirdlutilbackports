package org.openmrs.module.chirdlutilbackports.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Error;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ObsAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ObsAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Program;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Session;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.State;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateMapping;
import org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService;

/**
 * Defines implementations of services used by this module
 * 
 * @author Tammy Dugan
 */
public class ChirdlUtilBackportsServiceImpl implements ChirdlUtilBackportsService {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private ChirdlUtilBackportsDAO dao;
	
	/**
	 * Empty constructor
	 */
	public ChirdlUtilBackportsServiceImpl() {
	}
	
	/**
	 * @return ChirdlUtilBackportsDAO
	 */
	public ChirdlUtilBackportsDAO getChirdlUtilBackportsDAO() {
		return this.dao;
	}
	
	/**
	 * Sets the DAO for this service. The dao allows interaction with the database.
	 * 
	 * @param dao
	 */
	public void setChirdlUtilBackportsDAO(ChirdlUtilBackportsDAO dao) {
		this.dao = dao;
	}
	
	public LocationTagAttributeValue getLocationTagAttributeValue(Integer locationTagId, String locationTagAttributeName,
	                                                              Integer locationId) {
		return getChirdlUtilBackportsDAO().getLocationTagAttributeValue(locationTagId, locationTagAttributeName, locationId);
	}
	
	public LocationAttributeValue getLocationAttributeValue(Integer locationId, String locationAttributeName) {
		return getChirdlUtilBackportsDAO().getLocationAttributeValue(locationId, locationAttributeName);
	}
	
	public LocationTagAttributeValue getLocationTagAttributeValueById(Integer location_tag_attribute_value_id) {
		return getChirdlUtilBackportsDAO().getLocationTagAttributeValueById(location_tag_attribute_value_id);
	}
	
	public LocationTagAttribute getLocationTagAttribute(Integer locationTagAttributeId) {
		return getChirdlUtilBackportsDAO().getLocationTagAttribute(locationTagAttributeId);
	}
	
	public LocationTagAttribute getLocationTagAttribute(String locationTagAttributeName) {
		return getChirdlUtilBackportsDAO().getLocationTagAttribute(locationTagAttributeName);
	}
	
	public LocationTagAttribute saveLocationTagAttribute(LocationTagAttribute value) {
		return getChirdlUtilBackportsDAO().saveLocationTagAttribute(value);
	}
	
	public LocationTagAttributeValue saveLocationTagAttributeValue(LocationTagAttributeValue value) {
		return getChirdlUtilBackportsDAO().saveLocationTagAttributeValue(value);
	}
	
	public LocationAttributeValue saveLocationAttributeValue(LocationAttributeValue value) {
		return getChirdlUtilBackportsDAO().saveLocationAttributeValue(value);
	}
	
	public void deleteLocationTagAttribute(LocationTagAttribute value) {
		getChirdlUtilBackportsDAO().deleteLocationTagAttribute(value);
	}
	
	public void deleteLocationTagAttributeValue(LocationTagAttributeValue value) {
		getChirdlUtilBackportsDAO().deleteLocationTagAttributeValue(value);
	}
	
	public FormInstance addFormInstance(Integer formId, Integer locationId) {
		return getChirdlUtilBackportsDAO().addFormInstance(formId, locationId);
	}
	
	public FormAttributeValue getFormAttributeValue(Integer formId, String formAttributeName, Integer locationTagId,
	                                                Integer locationId) {
		return getChirdlUtilBackportsDAO().getFormAttributeValue(formId, formAttributeName, locationTagId, locationId);
	}
	
	/**
	 * Get state mapping by initial state name
	 * 
	 * @param stateMappingId state mapping unique id
	 * @return state with given state name
	 */
	public StateMapping getStateMapping(State initialState,Program program)  {
		return getChirdlUtilBackportsDAO().getStateMapping(initialState,program);
	}
	
	public Session addSession() {
		
		Session session = new Session();
		
		return getChirdlUtilBackportsDAO().addSession(session);
	}
	
	public Session getSession(int sessionId)
	{
		return getChirdlUtilBackportsDAO().getSession(sessionId);
	}
		
	public Session updateSession(Session session) {
		
		return getChirdlUtilBackportsDAO().updateSession(session);
	}
	
	public PatientState addPatientState(Patient patient,State initialState,
			int sessionId,Integer locationTagId,Integer locationId)
	{
		PatientState patientState = new PatientState();
		patientState.setStartTime(new java.util.Date());
		patientState.setPatient(patient);
		patientState.setState(initialState);
		patientState.setSessionId(sessionId);
		patientState.setLocationId(locationId);
		patientState.setLocationTagId(locationTagId);
		return getChirdlUtilBackportsDAO().addUpdatePatientState(patientState);
	}
	
	public PatientState updatePatientState(PatientState patientState)
	{
		return getChirdlUtilBackportsDAO().addUpdatePatientState(patientState);
	}
		
	public List<PatientState> getPatientStatesWithForm(int sessionId){
		return getChirdlUtilBackportsDAO().getPatientStatesWithForm(sessionId);
	}
	
	public PatientState getPrevPatientStateByAction(
			int sessionId, int patientStateId,String action)
	{
		return getChirdlUtilBackportsDAO().getPrevPatientStateByAction(
				sessionId, patientStateId,action);
	}
	
	public List<PatientState> getUnfinishedPatientStatesAllPatients(Date optionalDateRestriction,
			Integer locationTagId,Integer locationId)
	{
		return getChirdlUtilBackportsDAO().getUnfinishedPatientStatesAllPatients(optionalDateRestriction,locationTagId,locationId);
	}
	
	public List<PatientState> getUnfinishedPatientStateByStateName(String state,Date optionalDateRestriction,
			Integer locationTagId, Integer locationId){
		return getChirdlUtilBackportsDAO().getUnfinishedPatientStateByStateName(state,optionalDateRestriction,locationTagId,locationId);
	}
	public PatientState getLastUnfinishedPatientState(Integer sessionId){
		return getChirdlUtilBackportsDAO().getLastUnfinishedPatientState(sessionId);
	}
	
	public PatientState getLastPatientState(Integer sessionId){
		return getChirdlUtilBackportsDAO().getLastPatientState(sessionId);
	}
	
	public List<PatientState> getLastPatientStateAllPatients(Date optionalDateRestriction, 
			Integer programId,String startStateName,Integer locationTagId, Integer locationId){
		return getChirdlUtilBackportsDAO().getLastPatientStateAllPatients(optionalDateRestriction, programId,startStateName, locationTagId,locationId);
	}
	public State getStateByName(String stateName){
		return getChirdlUtilBackportsDAO().getStateByName(stateName);
	}
	
	public Program getProgramByNameVersion(String name,String version){
		return getChirdlUtilBackportsDAO().getProgramByNameVersion(name, version);
	}
	
	public Program getProgram(Integer programId){
		return getChirdlUtilBackportsDAO().getProgram(programId);
	}
	
	public PatientState getPatientStateByEncounterFormAction(Integer encounterId, Integer formId, String action){
		return getChirdlUtilBackportsDAO().getPatientStateByEncounterFormAction(encounterId, formId, action);
	}
	
	public PatientState getPatientStateByFormInstanceAction(FormInstance formInstance,String action){

		return getChirdlUtilBackportsDAO().getPatientStateByFormInstanceAction(formInstance, action);
	}
	
	public List<FormAttributeValue> getFormAttributesByName(String attributeName){
		return getChirdlUtilBackportsDAO().getFormAttributesByName(attributeName);
	}
	
	public ArrayList<String> getFormAttributesByNameAsString(String attributeName){
		return getChirdlUtilBackportsDAO().getFormAttributesByNameAsString(attributeName);
	}
	
	public List<State> getStatesByActionName(String actionName){
		return getChirdlUtilBackportsDAO().getStatesByActionName(actionName);
	}
	
	public State getState(Integer stateId){
		return getChirdlUtilBackportsDAO().getState(stateId);
	}
	public PatientState getPatientState(Integer patientStateId){
		return getChirdlUtilBackportsDAO().getPatientState(patientStateId);
	}
	
	public List<PatientState> getPatientStateBySessionState(Integer sessionId,
			Integer stateId){
		return getChirdlUtilBackportsDAO().getPatientStateBySessionState(sessionId,stateId);
	}
	
	public List<PatientState> getAllRetiredPatientStatesWithForm(Date thresholdDate){
		return getChirdlUtilBackportsDAO().getAllRetiredPatientStatesWithForm(thresholdDate);
	}
	
	public List<Session> getSessionsByEncounter(Integer encounterId){
		return getChirdlUtilBackportsDAO().getSessionsByEncounter(encounterId);
	}
	
	public List<PatientState> getPatientStatesWithFormInstances(String formName, Integer encounterId){
		return getChirdlUtilBackportsDAO().getPatientStatesWithFormInstances(formName, encounterId);
	}

	public List<PatientState> getPatientStateByEncounterState(Integer encounterId,
			Integer stateId){
		return getChirdlUtilBackportsDAO().getPatientStateByEncounterState(encounterId,stateId);
	}
	
	public void saveError(Error error){
		getChirdlUtilBackportsDAO().saveError(error);
	}
	
	public List<Error> getErrorsByLevel(String errorLevel,Integer sessionId){
		return getChirdlUtilBackportsDAO().getErrorsByLevel(errorLevel, sessionId);
	}
	
	public Integer getErrorCategoryIdByName(String name){
		return getChirdlUtilBackportsDAO().getErrorCategoryIdByName(name);
	}
	
	public Program getProgram(Integer locationTagId,Integer locationId){
		return getChirdlUtilBackportsDAO().getProgram(locationTagId, locationId);
	}

	public List<FormAttributeValue> getFormAttributeValuesByValue(String value){
		return getChirdlUtilBackportsDAO().getFormAttributeValuesByValue(value);
	}
	
	public List<PatientState> getUnfinishedPatientStateByStateSession(
		String stateName,Integer sessionId){
		return getChirdlUtilBackportsDAO().getUnfinishedPatientStateByStateSession(stateName, sessionId);
	}
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state) {
		return getChirdlUtilBackportsDAO().getPatientStateByFormInstanceState(formInstance, state);
	}
	public List<PatientState> getPatientStatesByFormInstance(FormInstance formInstance, boolean isRetired) {
		return getChirdlUtilBackportsDAO().getPatientStatesByFormInstance(formInstance, isRetired);
	}
	
	public List<PatientState> getPatientStatesBySession(Integer sessionId,boolean isRetired){
		return getChirdlUtilBackportsDAO().getPatientStatesBySession(sessionId, isRetired);
	}

	
	public void unretireStatesBySessionId(Integer sessionId){
		List<PatientState> patientStates = 
			this.getPatientStatesBySession(sessionId,true);
		
		for(PatientState patientState:patientStates){
			patientState.setRetired(false);
			patientState.setDateRetired(null);
			this.updatePatientState(patientState);
		}
	}
	
	public void saveFormAttributeValue(FormAttributeValue value){
		getChirdlUtilBackportsDAO().saveFormAttributeValue(value);
	}
	
	public FormAttribute getFormAttributeByName(String formAttributeName){
		return getChirdlUtilBackportsDAO().getFormAttributeByName(formAttributeName);
	}

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getObsAttributeByName(java.lang.String)
	 */
    public ObsAttribute getObsAttributeByName(String obsAttributeName) {
	    return getChirdlUtilBackportsDAO().getObsAttributeByName(obsAttributeName);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getObsAttributesByName(java.lang.String)
	 */
    public List<ObsAttributeValue> getObsAttributesByName(String attributeName) {
	    return getChirdlUtilBackportsDAO().getObsAttributesByName(attributeName);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getObsAttributesByNameAsString(java.lang.String)
	 */
    public List<String> getObsAttributesByNameAsString(String attributeName) {
	    return getChirdlUtilBackportsDAO().getObsAttributesByNameAsString(attributeName);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getObsAttributeValue(java.lang.Integer, java.lang.String)
	 */
    public ObsAttributeValue getObsAttributeValue(Integer obsId, String obsAttributeName) {
	    return getChirdlUtilBackportsDAO().getObsAttributeValue(obsId, obsAttributeName);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getObsAttributeValuesByValue(java.lang.String)
	 */
    public List<ObsAttributeValue> getObsAttributeValuesByValue(String value) {
	    return getChirdlUtilBackportsDAO().getObsAttributeValuesByValue(value);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#saveObsAttributeValue(org.openmrs.module.chirdlutilbackports.hibernateBeans.ObsAttributeValue)
	 */
    public void saveObsAttributeValue(ObsAttributeValue value) {
		getChirdlUtilBackportsDAO().saveObsAttributeValue(value);
    }
}
