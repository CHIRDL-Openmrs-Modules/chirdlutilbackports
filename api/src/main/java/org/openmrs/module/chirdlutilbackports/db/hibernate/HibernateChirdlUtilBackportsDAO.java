package org.openmrs.module.chirdlutilbackports.db.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StringType;
import org.openmrs.CareSetting;
import org.openmrs.Encounter;
import org.openmrs.FieldType;
import org.openmrs.Form;
import org.openmrs.FormField;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ChirdlLocationAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ChirdlLocationAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.EncounterAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.EncounterAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Error;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ErrorCategory;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ObsAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ObsAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Program;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ProgramTagMap;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Session;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.State;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateAction;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateMapping;
import org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService;
import org.openmrs.module.chirdlutilbackports.util.ChirdlUtilBackportsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate implementations of ChirdlUtilBackports related database functions.
 * 
 * @author Tammy Dugan
 */
public class HibernateChirdlUtilBackportsDAO implements ChirdlUtilBackportsDAO {
	
	private static final int start_index = 0;

	private static final int end_index = 250;

	private static final Logger log = LoggerFactory.getLogger(HibernateChirdlUtilBackportsDAO.class);
	
	private static final String LOCATION_TAG_ID = "locationTagId";
	private static final String LOCATION_ID = "locationId";
	private static final String LOCATION_TAG_ATTR_ID = "locationTagAttributeId";
	private static final String FORM_ATTR_NAME = "formAttributeName";
	private static final String FORM_ATTR_VALUE = "formAttributeValue";
	private static final String FORM_ID = "formId";
	private static final String FORM_NAME = "formName";
	private static final String FORM_ATTR_ID = "formAttributeId";
	private static final String FORM_INSTANCE_ID = "formInstanceId";
	private static final String FORM_INSTANCE_ATTR_NAME = "formInstanceAttributeName";
	private static final String FORM_INSTANCE_ATTR_ID = "formInstanceAttributeId";
	private static final String SESSION_ID = "sessionId";
	private static final String RETIRED = "retired";
	private static final String PATIENT_STATE_ID = "patientStateId";
	private static final String PROGRAM_ID = "programId";
	private static final String ENCOUNTER_ID = "encounterId";
	private static final String STATE_ID = "stateId";
	private static final String START_TIME = "startTime";
	private static final String VALUE = "value";
	private static final String ATTR_NAME = "attributeName";
	
	/**
	 * Hibernate session factory
	 */
	private SessionFactory sessionFactory;
	
	/**
	 * Empty constructor
	 */
	public HibernateChirdlUtilBackportsDAO() {
	}
	
	/**
	 * Set session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
    public LocationTagAttributeValue getLocationTagAttributeValue(Integer locationTagId, String locationTagAttributeName,
	                                                              Integer locationId) {
		try {
			LocationTagAttribute locationTagAttribute = this.getLocationTagAttributeByName(locationTagAttributeName);
			
			if (locationTagAttribute != null) {
				Integer locationTagAttributeId = locationTagAttribute.getLocationTagAttributeId();
				
				String sql = "select * from chirdlutilbackports_location_tag_attribute_value where location_tag_id=:locationTagId and location_id=:locationId and location_tag_attribute_id=:locationTagAttributeId";
				SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
				
				qry.setInteger(LOCATION_TAG_ID, locationTagId);
				qry.setInteger(LOCATION_ID, locationId);
				qry.setInteger(LOCATION_TAG_ATTR_ID, locationTagAttributeId);
				qry.addEntity(LocationTagAttributeValue.class);
				
				List<LocationTagAttributeValue> list = qry.list();
				
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
				
			}
		}
		catch (Exception e) {
			log.error("Error in method getLocationTagAttributeValue", e);
		}
		return null;
	}
	
	private LocationTagAttribute getLocationTagAttributeByName(String locationTagAttributeName) {
		try {
			String sql = "select * from chirdlutilbackports_location_tag_attribute " + "where name=:locationTagAttributeName";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString("locationTagAttributeName", locationTagAttributeName);
			qry.addEntity(LocationTagAttribute.class);
			
			List<LocationTagAttribute> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			log.error("Error in method getLocationTagAttributeByName", e);
		}
		return null;
	}
	
	@Override
	public ChirdlLocationAttributeValue getLocationAttributeValue(Integer locationId, String locationAttributeName) {
		try {
			ChirdlLocationAttribute locationAttribute = this.getLocationAttribute(locationAttributeName);
			
			if (locationAttribute != null) {
				Integer locationAttributeId = locationAttribute.getLocationAttributeId();
				
				String sql = "select * from chirdlutilbackports_location_attribute_value where location_id=:locationId and location_attribute_id=:locationAttributeId";
				SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
				
				qry.setInteger(LOCATION_ID, locationId);
				qry.setInteger("locationAttributeId", locationAttributeId);
				qry.addEntity(ChirdlLocationAttributeValue.class);
				
				List<ChirdlLocationAttributeValue> list = qry.list();
				
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
				
			}
		}
		catch (Exception e) {
			log.error("Error in method getLocationAttributeValue", e);
		}
		return null;
	}
	
	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getLocationAttribute(java.lang.String)
	 */
	@Override
	public ChirdlLocationAttribute getLocationAttribute(String locationAttributeName) {
		try {
			String sql = "select * from chirdlutilbackports_location_attribute " + "where name=:locationAttributeName";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString("locationAttributeName", locationAttributeName);
			qry.addEntity(ChirdlLocationAttribute.class);
			
			List<ChirdlLocationAttribute> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			log.error("Error in method getLocationAttribute", e);
		}
		return null;
	}
	
    @Override
	public LocationTagAttributeValue getLocationTagAttributeValueById(Integer location_tag_attribute_value_id) {
		try {
			String sql = "select * from chirdlutilbackports_location_tag_attribute_value "
			        + "where location_tag_attribute_value_id=:locationTagAttrValId";
			
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger("locationTagAttrValId", location_tag_attribute_value_id);
			qry.addEntity(LocationTagAttributeValue.class);
			List<LocationTagAttributeValue> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			log.error("Error in method getLocationTagAttributeValueById", e);
		}
		return null;
	}
	
	@Override
	public LocationTagAttribute getLocationTagAttribute(Integer locationTagAttributeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LocationTagAttribute.class).add(
		    Restrictions.eq(LOCATION_TAG_ATTR_ID, locationTagAttributeId));
		
		List<LocationTagAttribute> locations = criteria.list();
		if (null == locations || locations.isEmpty()) {
			return null;
		}
		return locations.get(0);
	}
	
	@Override
	public LocationTagAttribute getLocationTagAttribute(String locationTagAttributeName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LocationTagAttribute.class).add(
				Restrictions.eq("name", locationTagAttributeName));
		
		List<LocationTagAttribute> locations = criteria.list();
		if (null == locations || locations.isEmpty()) {
			return null;
		}
		return locations.get(0);
	}
	
	@Override
	public LocationTagAttribute saveLocationTagAttribute(LocationTagAttribute value) {
		sessionFactory.getCurrentSession().saveOrUpdate(value);
		return value;
	}
	
	@Override
	public LocationTagAttributeValue saveLocationTagAttributeValue(LocationTagAttributeValue value) {
		sessionFactory.getCurrentSession().saveOrUpdate(value);
		return value;
	}
	
	@Override
	public ChirdlLocationAttributeValue saveLocationAttributeValue(ChirdlLocationAttributeValue value) {
		sessionFactory.getCurrentSession().saveOrUpdate(value);
		return value;
	}
	
	@Override
	public void deleteLocationTagAttribute(LocationTagAttribute value) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LocationTagAttributeValue.class).add(
				Restrictions.eq(LOCATION_TAG_ATTR_ID, value.getLocationTagAttributeId()));
		
		List<LocationTagAttributeValue> locations = criteria.list();
		if (null != locations) {
			for (LocationTagAttributeValue attr : locations) {
				deleteLocationTagAttributeValue(attr);
			}
		}
		
		sessionFactory.getCurrentSession().delete(value);
	}
	
	@Override
	public void deleteLocationTagAttributeValue(LocationTagAttributeValue value) {
		sessionFactory.getCurrentSession().delete(value);
	}
	
	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#addFormInstance(
	 * java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public FormInstance addFormInstance(Integer formId, Integer locationId) {
		FormInstance formInstance = new FormInstance(locationId, formId, null);
		this.sessionFactory.getCurrentSession().save(formInstance);
		return formInstance;
	}
	
	@Override
	public FormAttribute getFormAttributeByName(String formAttributeName) {
		try {
			String sql = "select * from chirdlutilbackports_form_attribute where name=:formAttributeName";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(FORM_ATTR_NAME, formAttributeName);
			qry.addEntity(FormAttribute.class);
			
			List<FormAttribute> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			log.error("Error in method getFormAttributeByName", e);
		}
		return null;
	}
	
	@Override
	public State getStateByName(String stateName) {
		try {
			String sql = "select * from chirdlutilbackports_state where name=:stateName";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString("stateName", stateName);
			qry.addEntity(State.class);
			
			List<State> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			log.error("Error in method getStateByName", e);
		}
		return null;
	}
	
	/**
	 * 	@see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getFormAttributeValue(Integer formId, String formAttributeName, Integer locationTagId,Integer locationId)
	 **/
	@Override
	public FormAttributeValue getFormAttributeValue(Integer formId, String formAttributeName, Integer locationTagId,
	                                                Integer locationId) {
		try {
			FormAttribute formAttribute = this.getFormAttributeByName(formAttributeName);
			if (formAttribute == null) {
				return null;
			}
			
			return getFormAttributeValue(formId, formAttribute.getFormAttributeId(), locationTagId, locationId);
		}
		catch (Exception e) {
			log.error("Error in method getFormAttributeValue", e);
		}
		return null;
	}
	
	/**
	 * 	@see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getFormAttributeValue(Integer formId, Integer formAttributeId, Integer locationTagId,Integer locationId)
	 **/
	@Override
	public FormAttributeValue getFormAttributeValue(Integer formId, Integer formAttributeId, Integer locationTagId, Integer locationId) {
		try {	
				String sql = "select * from chirdlutilbackports_form_attribute_value where form_id=:formId "
				        + "and form_attribute_id=:formAttributeId and location_tag_id=:locationTagId and location_id=:locationId";
				SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
				
                qry.setInteger(FORM_ID, formId);
                qry.setInteger(FORM_ATTR_ID, formAttributeId);
                qry.setInteger(LOCATION_TAG_ID, locationTagId);
                qry.setInteger(LOCATION_ID, locationId);
				qry.addEntity(FormAttributeValue.class);
				
				List<FormAttributeValue> list = qry.list();
				
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
				
			
		}
		catch (Exception e) {
			log.error("Error in method getFormAttributeValue", e);
		}
		return null;
	}
	
	

	@Override
	public List<FormAttributeValue> getFormAttributeValues(Integer formId, Integer formAttributeId, Integer locationTagId, Integer locationId) {
		try {
			String sql = "select * from chirdlutilbackports_form_attribute_value where form_id=:formId " + "and form_attribute_id=:formAttributeId and location_tag_id=:locationTagId and location_id=:locationId";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);

			qry.setInteger(FORM_ID, formId);
			qry.setInteger(FORM_ATTR_ID, formAttributeId);
			qry.setInteger(LOCATION_TAG_ID, locationTagId);
			qry.setInteger(LOCATION_ID, locationId);
			qry.addEntity(FormAttributeValue.class);

			List<FormAttributeValue> list = qry.list();
			
			return list;

		} catch (Exception e) {
			log.error("Error in method getFormAttributeValue", e);
		}
		return null;
	}

	@Override
	public Session getSession(int sessionId) {
		try {
			String sql = "select * from chirdlutilbackports_session where session_id=:sessionId";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(SESSION_ID, sessionId);
			qry.addEntity(Session.class);
			return (Session) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error("Error in method getSession", e);
		}
		return null;
	}

	@Override
	public List<PatientState> getPatientStatesWithForm(int sessionId) {
		try {
			String sql = "select * from chirdlutilbackports_patient_state where session_id=:sessionId and form_id is not null and retired=:retired order by start_time desc,end_time desc";
			SQLQuery qry = null;
			
			qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(SESSION_ID, sessionId);
			qry.setBoolean(RETIRED, false);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getPatientStatesWithForm", e);
		}
		return null;
	}

	@Override
	public PatientState getPrevPatientStateByAction(int sessionId, int patientStateId, String action) {
		try {
			String sql = "select * from chirdlutilbackports_patient_state where session_id=:sessionId and patient_state_id < :patientStateId and retired=:retired"
			        + " order by patient_state_id desc";
			SQLQuery qry = null;
			qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(SESSION_ID, sessionId);
			qry.setInteger(PATIENT_STATE_ID, patientStateId);
			qry.setBoolean(RETIRED, false);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			List<PatientState> patientStates = qry.list();
			StateAction stateAction = null;
			
			for (PatientState patientState : patientStates) {
				stateAction = patientState.getState().getAction();
				if (stateAction != null) {
					if (stateAction.getActionName().equalsIgnoreCase(action)) {
						return patientState;
					}
				}
			}
		}
		catch (Exception e) {
			log.error("Error in method getPrevPatientStateByAction", e);
		}
		
		return null;
	}

	@Override
	public StateMapping getStateMapping(State initialState, Program program) {
		try {
			String sql = "select * from chirdlutilbackports_state_mapping where initial_state=:initialState and program_id=:programId";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger("initialState", initialState.getStateId());
			qry.setInteger(PROGRAM_ID, program.getProgramId());
			qry.addEntity(StateMapping.class);
			return (StateMapping) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error("Error in method getStateMapping", e);
		}
		return null;
	}
	
	@Override
	public Program getProgramByNameVersion(String name, String version) {
		try {
			String sql = "select * from chirdlutilbackports_program where name=:programName and version=:version";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString("programName", name);
			qry.setString("version", version);
			qry.addEntity(ChirdlUtilBackportsConstants.PROGRAM_ENTITY);
			return (Program) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error("Error in method getProgramByNameVersion", e);
		}
		return null;
	}
	
	@Override
	public Program getProgram(Integer programId) {
		try {
			String sql = "select * from chirdlutilbackports_program where program_id=:programId";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(PROGRAM_ID, programId);
			qry.addEntity(ChirdlUtilBackportsConstants.PROGRAM_ENTITY);
			return (Program) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error("Error in method getProgram", e);
		}
		return null;
	}
	
	@Override
	public Session addSession(Session session) {
		try {
			this.sessionFactory.getCurrentSession().save(session);
			return session;
		}
		catch (Exception e) {
			log.error("Error in method addSession", e);
		}
		return null;
	}
	
	@Override
	public Session updateSession(Session session) {
		try {
			this.sessionFactory.getCurrentSession().save(session);
			return session;
		}
		catch (Exception e) {
			log.error("Error in method updateSession", e);
		}
		return null;
	}
	
	@Override
	public PatientState addUpdatePatientState(PatientState patientState) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY, patientState);		
		return patientState;
	}
	
	@Override
	public StateAction getStateActionByName(String action) {
		try {
			String sql = "select * from chirdlutilbackports_state_action where action_name=:action";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString("action", action);
			qry.addEntity(StateAction.class);
			return (StateAction) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error("Error in method getStateActionByName", e);
		}
		return null;
	}
	
	@Override
	public List<PatientState> getPatientStateByEncounterState(Integer encounterId, Integer stateId) {
		try {
			String sql = "select a.* from chirdlutilbackports_patient_state a inner join chirdlutilbackports_session b on a.session_id=b.session_id "
			        + " where b.encounter_id=:encounterId and " + "a.state=:stateId order by start_time desc, end_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(ENCOUNTER_ID, encounterId);
			qry.setInteger(STATE_ID, stateId);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getPatientStateByEncounterState", e);
		}
		
		return null;
	}
	
	@Override
	public List<PatientState> getPatientStateBySessionState(Integer sessionId, Integer stateId) {
		
		try {
			String sql = "select * from chirdlutilbackports_patient_state where session_id=:sessionId and state=:stateId and retired=:retired order by start_time desc, end_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(SESSION_ID, sessionId);
			qry.setInteger(STATE_ID, stateId);
			qry.setBoolean(RETIRED, false);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			
			return qry.list();
			
		}
		catch (Exception e) {
			log.error("Error in method getPatientStateBySessionState", e);
		}
		return null;
	}
	
	@Override
	public List<PatientState> getPatientStatesBySession(Integer sessionId, boolean isRetired) {
		
		try {
			String sql = "select * from chirdlutilbackports_patient_state where session_id=:sessionId and retired=:retired order by start_time desc, end_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(SESSION_ID, sessionId);
			qry.setBoolean(RETIRED, isRetired);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			
			return qry.list();
			
		}
		catch (Exception e) {
			log.error("Error in method getPatientStatesBySession", e);
		}
		return null;
	}
	
	@Override
	public PatientState getPatientStateByEncounterFormAction(Integer encounterId, Integer formId, String action) {
		
		try {
			// limit to states for the session that match the form id
			String sql = "select a.* from chirdlutilbackports_patient_state a inner join chirdlutilbackports_session b on a.session_id=b.session_id "
			        + " where b.encounter_id=:encounterId " + "and a.form_id=:formId and a.retired=:retired order by start_time desc, end_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(ENCOUNTER_ID, encounterId);
			qry.setInteger(FORM_ID, formId);
			qry.setBoolean(RETIRED, false);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			
			List<PatientState> states = qry.list();
			
			// return the most recent state with the given action
			for (PatientState state : states) {
				StateAction stateAction = state.getState().getAction();
				if (stateAction != null && stateAction.getActionName().equalsIgnoreCase(action)) {
					return state;
				}
			}
		}
		catch (Exception e) {
			log.error("Error in method getPatientStateByEncounterFormAction", e);
		}
		return null;
	}
	
	@Override
	public List<PatientState> getPatientStatesByFormInstance(FormInstance formInstance, boolean includeRetired) {
		
		try {
		    String retiredString = " ";
            
            if(!includeRetired){
                retiredString = " and retired=:retired ";
            }
		    
			// limit to states for the session that match the form id
			String sql = "select * from chirdlutilbackports_patient_state where form_instance_id=:formInstanceId "
			        + "and form_id=:formId and location_id=:locationId "+retiredString + "order by start_time desc, end_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(FORM_INSTANCE_ID, formInstance.getFormInstanceId());
			qry.setInteger(FORM_ID, formInstance.getFormId());
			qry.setInteger(LOCATION_ID, formInstance.getLocationId());
			
			if(!includeRetired){
			    qry.setBoolean(RETIRED, false);
			}

			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getPatientStatesByFormInstance", e);
		}
		return null;
	}
	
	@Override
	public PatientState getPatientStateByFormInstanceAction(FormInstance formInstance, String action, boolean includeRetired) {
		
		try {
			// limit to states for the session that match the form id
			List<PatientState> states = getPatientStatesByFormInstance(formInstance, includeRetired);
			
			// return the most recent state with the given action
			for (PatientState state : states) {
				StateAction stateAction = state.getState().getAction();
				if (stateAction != null && stateAction.getActionName().equalsIgnoreCase(action)) {
					return state;
				}
			}
		}
		catch (Exception e) {
			log.error("Error in method getPatientStateByFormInstanceAction", e);
		}
		return null;
	}
	
	@Override
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state) {
		return getPatientStateByFormInstanceState(formInstance,state,false);
	}

	
	@Override
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state, boolean includeRetired) {
		try {
			Integer stateId = state.getStateId();
			String retiredString = " ";
			
			if(!includeRetired){
				retiredString = " and retired=:retired ";
			}
			// limit to states for the session that match the form id
			String sql = "select * from chirdlutilbackports_patient_state where form_instance_id=:formInstanceId "
			        + "and form_id=:formId and location_id=:locationId"+retiredString+"and state=:stateId "
			        + "order by start_time desc, end_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(FORM_INSTANCE_ID, formInstance.getFormInstanceId());
			qry.setInteger(FORM_ID, formInstance.getFormId());
			qry.setInteger(LOCATION_ID, formInstance.getLocationId());
			if(includeRetired){
				qry.setInteger(STATE_ID, stateId);
			}else{
				qry.setBoolean(RETIRED, false);
				qry.setInteger(STATE_ID, stateId);
			}
			
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getPatientStateByFormInstanceState", e);
		}
		
		return null;
	}
	
	@Override
	public List<PatientState> getUnfinishedPatientStateByStateName(String stateName, Date optionalDateRestriction,
	                                                               Integer locationTagId, Integer locationId) {
		try {
			State state = this.getStateByName(stateName);
			String dateRestriction = "";
			if (optionalDateRestriction != null) {
				dateRestriction = " and start_time >= :startTime";
			}
			String sql = "select * from chirdlutilbackports_patient_state where state in "
			        + "(:stateId) and end_time is null and retired=:retired and location_tag_id=:locationTagId " + " and location_id=:locationId "
			        + dateRestriction + " order by start_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(STATE_ID, state.getStateId());
			qry.setBoolean(RETIRED, false);
			qry.setInteger(LOCATION_TAG_ID, locationTagId);
			qry.setInteger(LOCATION_ID, locationId);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			if (optionalDateRestriction != null) {
				qry.setDate(START_TIME, optionalDateRestriction);
			}
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getUnfinishedPatientStateByStateName", e);
		}
		return null;
	}
	
	@Override
	public List<PatientState> getUnfinishedPatientStateByStateSession(String stateName, Integer sessionId) {
		try {
			State state = this.getStateByName(stateName);
			
			String sql = "select * from chirdlutilbackports_patient_state where state in "
			        + "(:stateId) and end_time is null and retired=:retired and session_id=:sessionId order by start_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(STATE_ID, state.getStateId());
			qry.setBoolean(RETIRED, false);
			qry.setInteger(SESSION_ID, sessionId);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getUnfinishedPatientStateByStateSession", e);
		}
		return null;
	}
	
	@Override
	public List<PatientState> getUnfinishedPatientStatesAllPatients(Date optionalDateRestriction, Integer locationTagId,
	                                                                Integer locationId) {
		try {
			String dateRestriction = "";
			if (optionalDateRestriction != null) {
				dateRestriction = " and start_time >= :startTime";
			}
			String sql = "select * from chirdlutilbackports_patient_state where end_time is null "
			        + "and retired=:retired and location_tag_id=:locationTagId and location_id=:locationId" + dateRestriction
			        + " order by start_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			qry.setBoolean(RETIRED, false);
			qry.setInteger(LOCATION_TAG_ID, locationTagId);
			qry.setInteger(LOCATION_ID, locationId);
			if (optionalDateRestriction != null) {
				qry.setDate(START_TIME, optionalDateRestriction);
			}
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getUnfinishedPatientStatesAllPatients", e);
		}
		return null;
	}
	
	public PatientState getLastUnfinishedPatientState(Integer sessionId) {
		try {
			String sql = "select * from chirdlutilbackports_patient_state where session_id=:sessionId "
			        + " and end_time is null and retired=:retired order by start_time desc, patient_state_id desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(SESSION_ID, sessionId);
			qry.setBoolean(RETIRED, false);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			List<PatientState> states = qry.list();
			if (states != null && states.size() > 0) {
				return states.get(0);
			}
			return null;
		}
		catch (Exception e) {
			log.error("Error in method getLastUnfinishedPatientState", e);
		}
		return null;
	}
	
	@Override
	public PatientState getLastPatientState(Integer sessionId) {
		try {
			String sql = "select * from chirdlutilbackports_patient_state where session_id=:sessionId "
			        + "  and retired=:retired  order by start_time desc,end_time desc, patient_state_id desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(SESSION_ID, sessionId);
			qry.setBoolean(RETIRED, false);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			List<PatientState> states = qry.list();
			if (states != null && states.size() > 0) {
				return states.get(0);
			}
			return null;
		}
		catch (Exception e) {
			log.error("Error in method getLastPatientState", e);
		}
		return null;
	}
	
	@Override
	public State getState(Integer stateId) {
		try {
			String sql = "select * from chirdlutilbackports_state where state_id=:stateId";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(STATE_ID, stateId);
			qry.addEntity(State.class);
			
			return (State) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error("Error in method getState", e);
		}
		return null;
	}
	
	private List<String> getListMappedStates(Integer programId, String startStateName) {
		
		List<String> orderedStateNames = new ArrayList<String>();
		String sql = "Select * from chirdlutilbackports_state_mapping where program_id=:programId";
		SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		qry.addEntity(StateMapping.class);
		qry.setInteger(PROGRAM_ID, programId);
		List<StateMapping> mappings = qry.list();
		
		HashMap<String, StateMapping> stateMap = new HashMap<String, StateMapping>();
		
		for (StateMapping mapping : mappings) {
			stateMap.put(mapping.getInitialState().getName(), mapping);
		}
		
		StateMapping mapping = null;
		
		if (startStateName != null) {
			orderedStateNames.add(startStateName);
			mapping = stateMap.get(startStateName);
			
			while (mapping != null) {
				startStateName = mapping.getNextState().getName();
				orderedStateNames.add(startStateName);
				mapping = stateMap.get(startStateName);
			}
		}
		return orderedStateNames;
	}
	
	/**
	 * Please DONT pass the program object to this method. I received lazy initialization exceptions
	 * from the greaseboard when I did this. tmdugan
	 * 
	 * @param optionalDateRestriction
	 * @param programId
	 * @param startStateName
	 * @param locationTagId
	 * @return
	 */
	@Override
	public List<PatientState> getLastPatientStateAllPatients(Date optionalDateRestriction, Integer programId,
	                                                         String startStateName, Integer locationTagId, Integer locationId) {
		LinkedHashMap<Integer, LinkedHashMap<String, PatientState>> patientStateMap = new LinkedHashMap<Integer, LinkedHashMap<String, PatientState>>();
		Map<Integer, Integer> sessionToEncounterMap = new HashMap<Integer, Integer>();
		
		try {
			String dateRestriction = "";
			if (optionalDateRestriction != null) {
				dateRestriction = " and start_time >= :optionalDateRestriction";
			}
			
			String sql = "select aps.* from chirdlutilbackports_patient_state aps, chirdlutilbackports_session cs, encounter e"
			        + " where aps.session_id = cs.session_id and cs.encounter_id = e.encounter_id "
			        + " and retired=:retired and location_tag_id=:locationTagId and aps.location_id=:locationId " + dateRestriction
			        + " order by e.encounter_datetime desc,start_time desc";
			
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			
			qry.setBoolean(RETIRED, false);
			qry.setInteger(LOCATION_TAG_ID, locationTagId);
			qry.setInteger(LOCATION_ID, locationId);
			if (optionalDateRestriction != null) {
				qry.setDate("optionalDateRestriction", optionalDateRestriction);
			}
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			List<PatientState> states = qry.list();
			
			return findLatestUnfinishedPatientStates(states, programId, startStateName); // DWE CHICA-761 Moved code to method
			
		}
		catch (Exception e) {
			log.error("Error in method getLastPatientStateAllPatients", e);
		} finally {
			patientStateMap.clear();
			sessionToEncounterMap.clear();
		}
		return null;
	}
	
	@Override
	public List<FormAttributeValue> getFormAttributesByName(String attributeName) {
		try {
			String sql = "select * from chirdlutilbackports_form_attribute_value where form_attribute_id = "
			        + "(select form_attribute_id from chirdlutilbackports_form_attribute where name=:formAttributeName)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addEntity(FormAttributeValue.class);
			qry.setString(FORM_ATTR_NAME, attributeName);
			return qry.list();
			
		}
		catch (Exception e) {
			log.error("Error in method getFormAttributesByName", e);
		}
		return null;
	}
	
	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getFormAttributes(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@SuppressWarnings("unchecked")
    public List<FormAttributeValue> getFormAttributeValues(Integer attributeId, Integer locationId, Integer locationTagId) {
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FormAttributeValue.class).add(
					Restrictions.eq(FORM_ATTR_ID, attributeId));
			if (locationId != null) {
				criteria = criteria.add(Restrictions.eq(LOCATION_ID, locationId));
			}
			
			if (locationTagId != null) {
				criteria = criteria.add(Restrictions.eq(LOCATION_TAG_ID, locationTagId));
			}
			
			return criteria.list();
		}
		catch (Exception e) {
			log.error("Error in method getFormAttributeValues", e);
		}
		return null;
	}
	
	public ArrayList<String> getFormAttributesByNameAsString(String attributeName) {
		try {
			String sql = "select distinct value from chirdlutilbackports_form_attribute_value where form_attribute_id in "
			        + "(select form_attribute_id from chirdlutilbackports_form_attribute where name=:formAttributeName)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addScalar(VALUE);
			qry.setString(FORM_ATTR_NAME, attributeName);
			List<String> list = qry.list();
			
			ArrayList<String> exportDirectories = new ArrayList<String>();
			for (String currResult : list) {
				exportDirectories.add(currResult);
			}
			
			return exportDirectories;
		}
		catch (Exception e) {
			log.error("Error in method getFormAttributesByNameAsString", e);
		}
		return null;
	}
	
	@Override
	public List<State> getStatesByActionName(String actionName) {
		try {
			String sql = "select * from chirdlutilbackports_state where state_action_id="
			        + "(select state_action_id from chirdlutilbackports_state_action where action_name=:stateActionName)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString("stateActionName", actionName);
			qry.addEntity(State.class);
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getStatesByActionName", e);
		}
		return null;
	}
	
	@Override
	public PatientState getPatientState(Integer patientStateId) {
		try {
			String sql = "select * from chirdlutilbackports_patient_state where patient_state_id=:patientStateId";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(PATIENT_STATE_ID, patientStateId);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			return (PatientState) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error("Error in method getPatientState", e);
		}
		return null;
	}
	
	@Override
	public List<PatientState> getAllRetiredPatientStatesWithForm(Date thresholdDate) {
		try {
			String sql = "select * from chirdlutilbackports_patient_state where form_id is not null and form_instance_id is not null "
			        + "and retired=:retired and start_time < :startTime";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			
			qry.setBoolean(RETIRED, true);
			qry.setDate(START_TIME, thresholdDate);
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getAllRetiredPatientStatesWithForm", e);
		}
		return null;
	}
	
	@Override
	public List<Session> getSessionsByEncounter(int encounterId) {
		try {
			String sql = "select * from chirdlutilbackports_session where encounter_id=:encounterId ";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(ENCOUNTER_ID, encounterId);
			qry.addEntity(Session.class);
			return qry.list();
			
		}
		catch (Exception e) {
			log.error("Error in method getSessionsByEncounter", e);
		}
		return null;
	}
	
	@Override
	public Program getProgram(Integer locationTagId, Integer locationId) {
		try {
			String sql = "select * from chirdlutilbackports_program_tag_map where location_id=:locationId and location_tag_id=:locationTagId";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(LOCATION_ID, locationId);
			qry.setInteger(LOCATION_TAG_ID, locationTagId);
			qry.addEntity(ProgramTagMap.class);
			ProgramTagMap map = (ProgramTagMap) qry.uniqueResult();
			
			if (map != null) {
				return map.getProgram();
			}
			
		}
		catch (Exception e) {
			log.error("Error in method getProgram", e);
		}
		return null;
	}
	
	@Override
	public List<PatientState> getPatientStatesWithFormInstances(String formName, Integer encounterId) {
		
		SQLQuery qry = null;
		
		if (formName != null) {
			String sql = "select a.* from chirdlutilbackports_patient_state a " + "inner join chirdlutilbackports_session b on a.session_id=b.session_id "
			        + "inner join form c on a.form_id=c.form_id where " + "b.encounter_id=:encounterId and c.name=:formName and "
			        + "form_instance_id is not null order by end_time desc";
			qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(ENCOUNTER_ID, encounterId);
			qry.setString(FORM_NAME, formName);
		} else {
			String sql = "select a.* from chirdlutilbackports_patient_state a " + "inner join chirdlutilbackports_session b on a.session_id=b.session_id "
			        + "where b.encounter_id=:encounterId and " + "form_instance_id is not null order by end_time desc";
			qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(ENCOUNTER_ID, encounterId);
		}
		
		qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
		return qry.list();
	}
	
	@Override
	public void saveError(Error error) {
		try {
			//MESHELEY - CHICA-659: limit error message string size to less than size of message column
			if (error != null ){
			   String message = error.getMessage();
			   if (message != null && message.length() > (end_index - start_index)){
				   error.setMessage(message.substring(start_index, end_index));
			   }
			   this.sessionFactory.getCurrentSession().save(error);
			}
		}
		catch (Exception e) {
			log.error("Error in method saveError", e);
		}
	}
	
	@Override
	public List<Error> getErrorsByLevel(String errorLevel, Integer sessionId) {
		try {
			String sql = "select * from chirdlutilbackports_error where level=:errorLevel and session_id=:sessionId";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString("errorLevel", errorLevel);
			qry.setInteger(SESSION_ID, sessionId);
			qry.addEntity(Error.class);
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getErrorsByLevel", e);
		}
		return null;
	}
	
	@Override
	public List<FormAttributeValue> getFormAttributeValuesByValue(String value) {
		try {
			String sql = "select * from chirdlutilbackports_form_attribute_value where value=:formAttributeValue";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(FORM_ATTR_VALUE, value);
			qry.addEntity(FormAttributeValue.class);
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getFormAttributeValuesByValue", e);
		}
		return null;
	}
	
	@Override
	public void saveFormAttributeValue(FormAttributeValue value) {
		sessionFactory.getCurrentSession().saveOrUpdate(value);
	}
	
	@Override
	public Integer getErrorCategoryIdByName(String name) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ErrorCategory.class).add(
				Restrictions.eq("name", name));
			try {
				List<ErrorCategory> categories = criteria.list();
				for (ErrorCategory category : categories){
					Integer id = category.getErrorCategoryId();
					return id;
				}
				
				log.warn("No error categories found with name: " + name);
				
			}catch (Exception e){
				log.error("Exception getting ErrorCategoryId by name", e);
			}
		
		return null;

}

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getObsAttributeByName(java.lang.String)
	 */
    @Override
	public ObsAttribute getObsAttributeByName(String obsAttributeName) {
		try {
			String sql = "select * from chirdlutilbackports_obs_attribute where name=:obsAttributeName";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString("obsAttributeName", obsAttributeName);
			qry.addEntity(ObsAttribute.class);
			
			List<ObsAttribute> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			log.error("Error in method getObsAttributeByName", e);
		}
		return null;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getObsAttributesByName(java.lang.String)
	 */
    @Override
	public List<ObsAttributeValue> getObsAttributesByName(String attributeName) {
    	try {
			String sql = "select * from chirdlutilbackports_obs_attribute_value where obs_attribute_id in "
			        + "(select obs_attribute_id from chirdlutilbackports_obs_attribute where name=:attributeName)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addEntity(ObsAttributeValue.class);
			qry.setString(ATTR_NAME, attributeName);
			return qry.list();
			
		}
		catch (Exception e) {
			log.error("Error in method getObsAttributesByName", e);
		}
		return null;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getObsAttributesByNameAsString(java.lang.String)
	 */
    @Override
	public List<String> getObsAttributesByNameAsString(String attributeName) {
    	try {
			String sql = "select distinct value from chirdlutilbackports_obs_attribute_value where obs_attribute_id in "
			        + "(select obs_attribute_id from chirdlutilbackports_obs_attribute where name=:attributeName)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addScalar(VALUE);
			qry.setString(ATTR_NAME, attributeName);
			List<String> list = qry.list();
			
			ArrayList<String> attributes = new ArrayList<String>();
			for (String currResult : list) {
				attributes.add(currResult);
			}
			
			return attributes;
		}
		catch (Exception e) {
			log.error("Error in method getObsAttributesByNameAsString", e);
		}
		return null;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getObsAttributeValue(java.lang.Integer, java.lang.String)
	 */
    @Override
	public ObsAttributeValue getObsAttributeValue(Integer obsId, String obsAttributeName) {
    	try {
			ObsAttribute obsAttribute = this.getObsAttributeByName(obsAttributeName);
			
			if (obsAttribute != null) {
				Integer obsAttributeId = obsAttribute.getObsAttributeId();
				
				String sql = "select * from chirdlutilbackports_obs_attribute_value where obs_id=:obsId "
				        + "and obs_attribute_id=:obsAttributeId";
				SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
				
				qry.setInteger("obsId", obsId);
				qry.setInteger("obsAttributeId", obsAttributeId);
				qry.addEntity(ObsAttributeValue.class);
				
				List<ObsAttributeValue> list = qry.list();
				
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
				
			}
		}
		catch (Exception e) {
			log.error("Error in method getObsAttributeValue", e);
		}
		return null;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getObsAttributeValuesByValue(java.lang.String)
	 */
    @Override
	public List<ObsAttributeValue> getObsAttributeValuesByValue(String value) {
    	try {
			String sql = "select * from chirdlutilbackports_obs_attribute_value where value=:value";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(VALUE, value);
			qry.addEntity(ObsAttributeValue.class);
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getObsAttributeValuesByValue", e);
		}
		return null;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#saveObsAttributeValue(org.openmrs.module.chirdlutilbackports.hibernateBeans.ObsAttributeValue)
	 */
    @Override
	public void saveObsAttributeValue(ObsAttributeValue value) {
    	sessionFactory.getCurrentSession().saveOrUpdate(value);
    }

    /**
     * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getFormInstanceAttributeByName(java.lang.String)
     */
    @Override
	public FormInstanceAttribute getFormInstanceAttributeByName(String formInstanceAttributeName) {
		try {
			String sql = "select * from chirdlutilbackports_form_instance_attribute where name=:formInstanceAttributeName";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(FORM_INSTANCE_ATTR_NAME, formInstanceAttributeName);
			qry.addEntity(FormInstanceAttribute.class);
			
			List<FormInstanceAttribute> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			log.error("Error in method getFormInstanceAttributeByName", e);
		}
		return null;
    }

    /**
     * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getFormInstanceAttributesByName(java.lang.String)
     */
	@Override
	public List<FormInstanceAttributeValue> getFormInstanceAttributesByName(String attributeName) {
		try {
			String sql = "select * from chirdlutilbackports_form_instance_attribute_value where "
					+ "form_instance_attribute_id in "
			        + "(select form_instance_attribute_id from chirdlutilbackports_form_instance_attribute where name=:formInstanceAttributeName)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addEntity(FormInstanceAttributeValue.class);
			qry.setString(FORM_INSTANCE_ATTR_NAME, attributeName);
			return qry.list();
			
		}
		catch (Exception e) {
			log.error("Error in method getFormInstanceAttributesByName", e);
		}
		return null;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getFormInstanceAttributesByNameAsString(java.lang.String)
	 */
    @Override
	public List<String> getFormInstanceAttributesByNameAsString(String attributeName) {
		try {
			String sql = "select distinct value from chirdlutilbackports_form_instance_attribute_value where "
					+ "form_instance_attribute_id in "
			        + "(select form_instance_attribute_id from chirdlutilbackports_form_instance_attribute where name=:formInstanceAttributeName)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addScalar(VALUE);
			qry.setString(FORM_INSTANCE_ATTR_NAME, attributeName);
			List<String> list = qry.list();
			
			ArrayList<String> attributes = new ArrayList<String>();
			for (String currResult : list) {
				attributes.add(currResult);
			}
			
			return attributes;
		}
		catch (Exception e) {
			log.error("Error in method getFormInstanceAttributesByNameAsString", e);
		}
		return null;
    }

    /**
     * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getFormInstanceAttributeValue(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
     */
    @Override
	public FormInstanceAttributeValue getFormInstanceAttributeValue(Integer formId, Integer formInstanceId,
                                                                    Integer locationId, String formInstanceAttributeName) {
		try {
			FormInstanceAttribute formInstanceAttribute = this.getFormInstanceAttributeByName(formInstanceAttributeName);
			
			if (formInstanceAttribute != null) {
				Integer formInstanceAttributeId = formInstanceAttribute.getFormInstanceAttributeId();
				
				String sql = "select * from chirdlutilbackports_form_instance_attribute_value where form_id=:formId "
				        + "and form_instance_id=:formInstanceId and location_id=:locationId and form_instance_attribute_id=:formInstanceAttributeId";
				SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
				
				qry.setInteger(FORM_ID, formId);
				qry.setInteger(FORM_INSTANCE_ID, formInstanceId);
				qry.setInteger(LOCATION_ID, locationId);
				qry.setInteger(FORM_INSTANCE_ATTR_ID, formInstanceAttributeId);
				qry.addEntity(FormInstanceAttributeValue.class);
				
				List<FormInstanceAttributeValue> list = qry.list();
				
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
				
			}
		}
		catch (Exception e) {
			log.error("Error in method getFormInstanceAttributeValue", e);
		}
		return null;
    }

    /**
     * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getFormInstanceAttributeValuesByValue(java.lang.String)
     */
    @Override
	public List<FormInstanceAttributeValue> getFormInstanceAttributeValuesByValue(String value) {
		try {
			String sql = "select * from chirdlutilbackports_form_instance_attribute_value where value=:formInstanceAttributeValue";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString("formInstanceAttributeValue", value);
			qry.addEntity(FormInstanceAttributeValue.class);
			return qry.list();
		}
		catch (Exception e) {
			log.error("Error in method getFormInstanceAttributeValuesByValue", e);
		}
		return null;
    }

    /**
     * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#saveFormInstanceAttributeValue(org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceAttributeValue)
     */
    @Override
	public void saveFormInstanceAttributeValue(FormInstanceAttributeValue value) {
		sessionFactory.getCurrentSession().saveOrUpdate(value);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getAllPrograms()
	 */
    @Override
	public List<Program> getAllPrograms() {
    	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ChirdlUtilBackportsConstants.PROGRAM_ENTITY);
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#saveProgram(org.openmrs.module.chirdlutilbackports.hibernateBeans.Program)
	 */
    @Override
	public Program saveProgram(Program program) {
		sessionFactory.getCurrentSession().saveOrUpdate(ChirdlUtilBackportsConstants.PROGRAM_ENTITY, program);
		return program;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#deleteProgram(org.openmrs.module.chirdlutilbackports.hibernateBeans.Program)
	 */
    @Override
	public void deleteProgram(Program program) {
		sessionFactory.getCurrentSession().delete(ChirdlUtilBackportsConstants.PROGRAM_ENTITY, program);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#saveProgramTagMap(org.openmrs.module.chirdlutilbackports.hibernateBeans.ProgramTagMap)
	 */
    @Override
	public ProgramTagMap saveProgramTagMap(ProgramTagMap programTagMap) {
		sessionFactory.getCurrentSession().saveOrUpdate(programTagMap);
		return programTagMap;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#deleteProgramTagMap(org.openmrs.module.chirdlutilbackports.hibernateBeans.ProgramTagMap)
	 */
    @Override
	public void deleteProgramTagMap(ProgramTagMap programTagMap) {
		sessionFactory.getCurrentSession().delete(programTagMap);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getProgram(java.lang.String)
	 */
    @Override
	public Program getProgram(String name) {
    	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ChirdlUtilBackportsConstants.PROGRAM_ENTITY).add(
    			Restrictions.eq("name", name));
		
		List<Program> programs = criteria.list();
		if (null == programs || programs.isEmpty()) {
			return null;
		}
		return programs.get(0);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getAllLocationAttributes()
	 */
    @Override
	public List<ChirdlLocationAttribute> getAllLocationAttributes() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ChirdlLocationAttribute.class);
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getAllLocationTagAttributes()
	 */
    @Override
	public List<LocationTagAttribute> getAllLocationTagAttributes() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LocationTagAttribute.class);
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getAllFormAttributes()
	 */
    @Override
	public List<FormAttribute> getAllFormAttributes() {
    	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FormAttribute.class);
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
    }
    
    /**
     * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getFormFields(org.openmrs.Form, java.util.List, boolean)
     */
	@Override
	public List<FormField> getFormFields(Form form, List<FieldType> fieldTypes, boolean ordered) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(FormField.class, "formField");
		criteria.createAlias("formField.field", "field");
		criteria.add(Restrictions.eq("form", form));
		if (fieldTypes != null && fieldTypes.size() > 0) {
			criteria.add(Restrictions.in("field.fieldType", fieldTypes));
		}
		
		if (ordered) {
			criteria.addOrder(Order.asc("fieldNumber"));
		}
		
		criteria.setFetchMode("field", FetchMode.JOIN);
		return criteria.list();
	}
	
	@Override
	public void deleteFormAttributeValue(FormAttributeValue fav) throws HibernateException {
		this.sessionFactory.getCurrentSession().delete(FormAttributeValue.class.getName(), fav);
	}

	
	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getEditableFormAttributes()
	 */
	@Override
	public List<FormAttribute> getEditableFormAttributes(){
		try{
		String sql = "select * from chirdlutilbackports_form_attribute";
		SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		qry.addEntity(FormAttribute.class);
		return qry.list();
		}
		catch(Exception e){
			log.error("Error in method HibernateChirdlUtilBackportsDAO.getEditableFormAttributes", e);
		}
		return null;
	}

	@Override
	public List<String> getCurrentFormAttributeValueStrCollection(FormAttribute fa) {
		List<String> valuesCollection;
		try{
			String sql = "select distinct value from chirdlutilbackports_form_attribute_value where form_attribute_id=:formAttributeId ";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setParameter(FORM_ATTR_ID, fa.getFormAttributeId());
			qry.addScalar(VALUE, new StringType());
			valuesCollection = qry.list();
			return valuesCollection;
		}
		catch(Exception e){
			log.error("Error in method HibernateChirdlUtilBackportsDAO.getCurrentFormAttributeValueCollection", e);
		}
		return null;
	}
	
	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getPersonAttributeByValue(java.lang.String, java.lang.String)
	 */
    @Override
	public PersonAttribute getPersonAttributeByValue(String personAttributeTypeName, String value) {		
		try {
			PersonAttributeType pat = Context.getPersonService().getPersonAttributeTypeByName(personAttributeTypeName);
			
			if (pat != null) {
				Integer personAttrTypeId = pat.getPersonAttributeTypeId();
				
				String sql = "select * from person_attribute where person_attribute_type_id=:personAttrTypeId and value=:personAttrValue";
				SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
				
				qry.setInteger("personAttrTypeId", personAttrTypeId);
				qry.setString("personAttrValue", value);
				qry.addEntity(PersonAttribute.class);
				
				List<PersonAttribute> list = qry.list();
				
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
			}
		}
		catch (Exception e) {
			log.error("Error in method getPersonAttributeByValue", e);
		}
		return null;
    }
	
	/**
	 * Returns the value of a form attribute from the chirdlutilbackports_form_attribute_value table.
	 * 
	 * @param formId id of the form to find an attribute for
	 * @param formAttribute the form attribute to use to find the value
	 * @param locationTagId the location tag id
	 * @param locationId the location id
	 * @return FormAttributeValue value of the attribute for the given form
	 */
    @Override
	public FormAttributeValue getFormAttributeValue(Integer formId, FormAttribute formAttribute, Integer locationTagId,
                                                    Integer locationId) {
    	if (formId == null || formAttribute == null || locationTagId == null || locationId == null) {
	    	return null;
	    }
	    
	    return getFormAttributeValue(formId, formAttribute.getFormAttributeId(), locationTagId, locationId);
    }
    
    /**
     * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getUsersByRole(org.openmrs.Role, boolean)
     */
    @Override
	@SuppressWarnings("unchecked")
    public List<User> getUsersByRole(Role role, boolean includeRetired) {
    	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class, "u");
    	if (!includeRetired) {
    		criteria.add(Restrictions.eq("u.retired", Boolean.FALSE));
    	}
    	
    	return criteria.createCriteria("roles", "r")
		        .add(Restrictions.eq("r.role", role.getRole())).list();
	}
	
	/**
	 * DWE CHICA-334 3/27/15
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getAllFormAttributeValuesByFormId(Integer)
	 */
	@Override
	public List<FormAttributeValue> getAllFormAttributeValuesByFormId(Integer formId)
	{
		List<FormAttributeValue> returnList = new ArrayList<FormAttributeValue>();
		
		try
		{
			String sql = "SELECT distinct a.form_id AS formId, " +
						 	"c.form_attribute_id AS formAttributeId, lt.location_tag_id AS locationTagId, " +
						 	"l.location_id AS locationId, b.value AS formAttributeValue " +
						 "FROM form a " +
						 "INNER JOIN chirdlutilbackports_form_attribute_value b ON a.form_id = b.form_id " +
						 "INNER JOIN location l ON b.location_id = l.location_id " +
						 "INNER JOIN location_tag lt ON b.location_tag_id = lt.location_tag_id " + 
						 "INNER JOIN chirdlutilbackports_form_attribute c ON b.form_attribute_id = c.form_attribute_id " +
						 "WHERE a.form_id=:formId";
			
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addScalar(FORM_ID);
			qry.addScalar(FORM_ATTR_ID);
			qry.addScalar(LOCATION_TAG_ID);
			qry.addScalar(LOCATION_ID);
			qry.addScalar(FORM_ATTR_VALUE);
				
			qry.setInteger(FORM_ID, formId);
		
			List<Object[]> list = qry.list();
			
			// Add FormAttributeValue objects to the return list
			for(Object[] objArray : list)
			{
				returnList.add(new FormAttributeValue((Integer)objArray[0], (Integer)objArray[1], (Integer)objArray[2], (Integer)objArray[3], (String)objArray[4]));
			}
		}
		catch(Exception e)
		{
			log.error("Error in method getAllFormAttributeValuesByFormId. Error loading FormAttributeValue list (formId = " + formId + ")", e);
		}
		
		return returnList;	
	}
	
	/**
	 * DWE CHICA-334 3/27/15
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getFormAttributeById(Integer)
	 */
	@Override
	public FormAttribute getFormAttributeById(Integer formAttributeId)
	{
		try 
		{
			String sql = "SELECT * FROM chirdlutilbackports_form_attribute WHERE form_attribute_id=:formAttributeId";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(FORM_ATTR_ID, formAttributeId);
			qry.addEntity(FormAttribute.class);
			
			List<FormAttribute> list = qry.list();
			
			if (list != null && list.size() > 0) 
			{
				return (FormAttribute)qry.uniqueResult();
			}
		}
		catch(Exception e)
		{
			log.error("Error in method getFormAttributeById. Error loading FormAttribute (form_attribute_id = " + formAttributeId + ")", e);
		}
		
		return new FormAttribute();
	}
	
	/**
	 * DWE CHICA-633
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getEncounterAttributeByName(String)
	 */
	@Override
	public EncounterAttribute getEncounterAttributeByName(String encounterAttributeName) throws HibernateException
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EncounterAttribute.class)
				.add(Restrictions.eq("name", encounterAttributeName));

		List<EncounterAttribute> list = criteria.list();
		
		if (list != null && list.size() > 0) 
		{
			return (EncounterAttribute)criteria.uniqueResult();
		}
		
		return null;
	}
	
	/**
	 * DWE CHICA-633
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#saveEncounterAttributeValue(EncounterAttributeValue)
	 */
	@Override
	public EncounterAttributeValue saveEncounterAttributeValue(EncounterAttributeValue encounterAttributeValue) throws HibernateException
	{
		sessionFactory.getCurrentSession().saveOrUpdate(encounterAttributeValue);
		return encounterAttributeValue;
	}
	
	/**
	 * DWE CHICA-633
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getEncounterAttributeValueByEncounterAttributeName(Integer, String)
	 */
	@Override
	public EncounterAttributeValue getEncounterAttributeValueByName(Integer encounterId, String encounterAttributeName, boolean includeVoided) throws HibernateException 
	{
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(EncounterAttributeValue.class, "encounterAttributeValue");
		Criteria nestedCriteria = criteria.createCriteria("encounterAttribute", "encounterAttribute");
		nestedCriteria.add(Restrictions.eq("encounterAttribute.name", encounterAttributeName));
		criteria.add(Restrictions.eq("encounterAttributeValue.encounterId", encounterId));
		if(!includeVoided)
		{
			criteria.add(Restrictions.eq("encounterAttributeValue.voided", false));
		}
		List<EncounterAttributeValue> list = criteria.list();

		if (list != null && !list.isEmpty()) 
		{
			return (EncounterAttributeValue)criteria.uniqueResult();
		}

		return null;
	}
	
	/**
	 * DWE CHICA-633
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getEncounterAttributeValueByAttribute(Integer, EncounterAttributeValue, boolean)
	 */
	@Override
	public EncounterAttributeValue getEncounterAttributeValueByAttribute(Integer encounterId, EncounterAttribute encounterAttribute, boolean includeVoided) throws HibernateException 
	{
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(EncounterAttributeValue.class, "encounterAttributeValue");
		criteria.add(Restrictions.eq("encounterAttributeValue.encounterId", encounterId));
		criteria.add(Restrictions.eq("encounterAttribute", encounterAttribute));
		
		if(!includeVoided)
		{
			criteria.add(Restrictions.eq("encounterAttributeValue.voided", false));
		}

		List<EncounterAttributeValue> list = criteria.list();

		if (list != null && list.size() > 0) 
		{
			return (EncounterAttributeValue)criteria.uniqueResult();
		}

		return null;
	}
	
	/**
	 * DWE CHICA-761
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getLastPatientStateAllPatientsByLocation(Date, Integer, String, Integer)
	 */
	@Override
	public List<PatientState> getLastPatientStateAllPatientsByLocation(Date optionalDateRestriction, Integer programId, String startStateName, Integer locationId) throws HibernateException 
	{
		String dateRestriction = "";
		if (optionalDateRestriction != null) {
			dateRestriction = " AND aps.start_time >= :optionalDateRestriction"; 
		}
		
		String sql = "SELECT aps.* FROM chirdlutilbackports_patient_state aps"
				+ " INNER JOIN chirdlutilbackports_session cs ON aps.session_id = cs.session_id" 
				+ " INNER JOIN encounter e ON cs.encounter_id = e.encounter_id"
				+ " WHERE aps.retired=:retired AND aps.location_id=:locationId" + dateRestriction
				+ " ORDER BY e.encounter_datetime DESC, aps.start_time DESC";
		
		SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		
		qry.setBoolean(RETIRED, false);
		qry.setInteger(LOCATION_ID, locationId); 
		if (optionalDateRestriction != null) {
			qry.setDate("optionalDateRestriction", optionalDateRestriction);
		} 
		qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
		List<PatientState> states = qry.list();
		
		return findLatestUnfinishedPatientStates(states, programId, startStateName);
	}
	
	/**
	 * DWE CHICA-761
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getProgramByLocation(Integer)
	 */
	@Override
	public Program getProgramByLocation(Integer locationId) throws HibernateException
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProgramTagMap.class).add(
				Restrictions.eq(LOCATION_ID, locationId));
		List<ProgramTagMap> list = criteria.list();
		
		if(list != null && list.size() > 0)
		{
			ProgramTagMap map = list.get(0);
			if(map != null)
			{
				return map.getProgram();
			}
		}
		
		return null;
	}
	
	/**
	 * DWE CHICA-761 Moved existing code into a method
	 * Returns a list of the latest unfinished states, one for each of the given patients
	 * @param states
	 * @param programId
	 * @param startStateName
	 * @return
	 */
	private List<PatientState> findLatestUnfinishedPatientStates(List<PatientState> states, Integer programId, String startStateName)
	{
		try
		{
			ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
			LinkedHashMap<Integer, LinkedHashMap<String, PatientState>> patientStateMap = new LinkedHashMap<Integer, LinkedHashMap<String, PatientState>>();
			Map<Integer, Integer> sessionToEncounterMap = new HashMap<Integer, Integer>();
			List<PatientState> patientStates = new ArrayList<PatientState>();

			for (PatientState patientState : states) {
				Integer sessionId = patientState.getSessionId();
				Integer encounterId = sessionToEncounterMap.get(sessionId);
				if (encounterId == null) {
					encounterId = chirdlUtilBackportsService.getSession(sessionId).getEncounterId();
					sessionToEncounterMap.put(sessionId, encounterId);
				}
				LinkedHashMap<String, PatientState> stateNameMap = patientStateMap.get(encounterId);
				if (stateNameMap == null) {
					stateNameMap = new LinkedHashMap<String, PatientState>();
				}
				if (stateNameMap.get(patientState.getState().getName()) == null) {
					stateNameMap.put(patientState.getState().getName(), patientState);
				}
				patientStateMap.put(encounterId, stateNameMap);
			}

			List<String> mappedStateNames = getListMappedStates(programId, startStateName);

			//look at the state chain in reverse order
			//find the latest unfinished state in the chain for the given patient
			for (Integer encounterId : patientStateMap.keySet()) {
				LinkedHashMap<String, PatientState> stateNameMap = patientStateMap.get(encounterId);
				for (int i = mappedStateNames.size() - 1; i >= 0; i--) {
					String currStateName = mappedStateNames.get(i);
					PatientState currPatientState = stateNameMap.get(currStateName);
					if (currPatientState != null) {
						patientStates.add(currPatientState);
						break;
					}
				}

				stateNameMap.clear();
			}
			
			return patientStates;	
		}
		catch(Exception e)
		{
			log.error("Error in method findLatestUnfinishedPatientStates.", e);
			return null;
		}	
	}
	
	/**
	 * DWE CHICA-633
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getEncounterAttributeValueByValue(String, String)
	 */
	@Override
	public EncounterAttributeValue getEncounterAttributeValueByValue(String attributeValue, String encounterAttributeName) throws HibernateException 
	{
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(EncounterAttributeValue.class, "encounterAttributeValue");
		Criteria nestedCriteria = criteria.createCriteria("encounterAttribute", "encounterAttribute");
		nestedCriteria.add(Restrictions.eq("encounterAttribute.name", encounterAttributeName));
		criteria.add(Restrictions.eq("encounterAttributeValue.valueText", attributeValue));
		criteria.add(Restrictions.eq("encounterAttributeValue.voided", false));
		
		List<EncounterAttributeValue> list = criteria.list();

		if (list != null && list.size() > 0) 
		{
			return (EncounterAttributeValue)criteria.uniqueResult();
		}

		return null;
	}
	
	/**
	 * CHICA-862
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getPatientStatesBySessionId(Integer, List, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
    public List<PatientState> getPatientStatesBySessionId(Integer sessionId, List<String> stateNames, boolean includeRetired) throws HibernateException
    {
           Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY, "ps");
           Criteria nestedCriteria = criteria.createCriteria("state", "s");
           nestedCriteria.add(Restrictions.in("s.name", stateNames));
           criteria.add(Restrictions.eq("ps.sessionId", sessionId));
           if(!includeRetired)
   			{ criteria.add(Restrictions.eq("ps.retired", Boolean.FALSE)); }

           criteria.addOrder(Order.desc("ps.startTime"));

           return criteria.list();
    }
	
	/**
	 * CHICA-993
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#deleteLocationTagAttributeValueByValue(LocationTagAttribute, String)
	 */
	@Override
	public void deleteLocationTagAttributeValueByValue(LocationTagAttribute locationTagAttribute, String value)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LocationTagAttributeValue.class)
				.add(Restrictions.eq(LOCATION_TAG_ATTR_ID, locationTagAttribute.getLocationTagAttributeId()))
				.add(Restrictions.eq(VALUE, value));
		
		List<LocationTagAttributeValue> locationTagAttributeValues = criteria.list();
		if (locationTagAttributeValues != null) 
		{
			for (LocationTagAttributeValue attrValue : locationTagAttributeValues) 
			{
				deleteLocationTagAttributeValue(attrValue);
			}
		}
	}
	
	/**
	 * CHICA-1169
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getPatientStatesByFormNameAndState(java.lang.String, java.util.List, java.lang.Integer, boolean)
	 */
	@Override
	public List<PatientState> getPatientStatesByFormNameAndState(String formName, List<String> stateNames, Integer encounterId, boolean includeRetired) throws DAOException
	{
		try
		{
			String encounerIdRestriction = "";
			if(encounterId != null)
			{
				encounerIdRestriction = " AND s.encounter_id = :encounterId";
			}
			
			String retiredRestriction = "";
			if(!includeRetired)
			{
				retiredRestriction = " AND ps.retired = :retired";
			}
			
			String formNameRestriction = "";
			if(formName != null)
			{
				formNameRestriction = " AND f.name = :formName";
			}
			
			String stateNameRestriction = "";
			if(stateNames != null && !stateNames.isEmpty())
			{
				stateNameRestriction = " AND st.name IN (:stateNames)";
			}

			String sql = "SELECT ps.* FROM chirdlutilbackports_patient_state ps"
					+ " INNER JOIN chirdlutilbackports_session s ON ps.session_id=s.session_id" 
					+ " INNER JOIN chirdlutilbackports_state st ON ps.state = st.state_id"
					+ " INNER JOIN form f ON ps.form_id=f.form_id"
					+ " WHERE ps.form_instance_id IS NOT NULL"
					+ encounerIdRestriction
					+ formNameRestriction
					+ stateNameRestriction
					+ retiredRestriction
					+ " ORDER BY ps.end_time DESC";

			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			
			if(encounterId != null)
			{
				qry.setInteger(ENCOUNTER_ID, encounterId);
			}
			
			if(formName != null)
			{
				qry.setString(FORM_NAME, formName);
			}
			
			if(stateNames != null && !stateNames.isEmpty())
			{
				qry.setParameterList("stateNames", stateNames);
			}
			
			if(!includeRetired)
			{
				qry.setBoolean(RETIRED, false);
			}
			
			qry.addEntity(ChirdlUtilBackportsConstants.PATIENT_STATE_ENTITY);
			
			return qry.list();
		}
		catch(Exception e)
		{
			log.error("Error in method getPatientStatesByFormNameAndState.", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getPeopleByBirthDate(
	 * java.util.Date, boolean)
	 */
	@Override
	public List<Person> getPeopleByBirthDate(Date birthDate, boolean includeVoided) {
		if (birthDate == null) {
			return new ArrayList<>();
		}
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Person.class);
		
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		
		criteria.add(Restrictions.eq("birthdate", birthDate));
		
		return criteria.list();
	}

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getOrders(
	 * org.openmrs.Patient, java.util.List, java.util.List, java.util.List, boolean)
	 */
	@Override
	public List<org.openmrs.Order> getOrders(Patient patient, List<CareSetting> careSettings, List<OrderType> orderTypes,
	        List<Encounter> encounters, boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(org.openmrs.Order.class);
		if (patient != null) {
			criteria.add(Restrictions.eq("patient", patient));
		}
		
		if (careSettings != null && careSettings.size() == 1) {
			criteria.add(Restrictions.eq("careSetting", careSettings.get(0)));
		} else if (careSettings != null && !careSettings.isEmpty()) {
			criteria.add(Restrictions.in("careSetting", careSettings));
		}
		
		if (orderTypes != null && orderTypes.size() == 1) {
			criteria.add(Restrictions.eq("orderType", orderTypes.get(0)));
		} else if (orderTypes != null && !orderTypes.isEmpty()) {
			criteria.add(Restrictions.in("orderType", orderTypes));
		}
		
		if (encounters != null && encounters.size() == 1) {
			criteria.add(Restrictions.eq("encounter", encounters.get(0)));
		} else if (encounters != null && !encounters.isEmpty()) {
			criteria.add(Restrictions.in("encounter", encounters));
		}
		
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", includeVoided));
		}
		
		return criteria.list();
	}
}