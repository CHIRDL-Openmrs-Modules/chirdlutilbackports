package org.openmrs.module.chirdlutilbackports.db.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.openmrs.Form;
import org.openmrs.api.FormService;
import org.openmrs.api.context.Context;
import org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Error;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationAttributeValue;
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

/**
 * Hibernate implementations of ChirdlUtilBackports related database functions.
 * 
 * @author Tammy Dugan
 */
public class HibernateChirdlUtilBackportsDAO implements ChirdlUtilBackportsDAO {
	
	protected final Log log = LogFactory.getLog(getClass());
	
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
	
	public LocationTagAttributeValue getLocationTagAttributeValue(Integer locationTagId, String locationTagAttributeName,
	                                                              Integer locationId) {
		try {
			LocationTagAttribute locationTagAttribute = this.getLocationTagAttributeByName(locationTagAttributeName);
			
			if (locationTagAttribute != null) {
				Integer locationTagAttributeId = locationTagAttribute.getLocationTagAttributeId();
				
				String sql = "select * from chirdlutilbackports_location_tag_attribute_value where location_tag_id=? and location_id=? and location_tag_attribute_id=?";
				SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
				
				qry.setInteger(0, locationTagId);
				qry.setInteger(1, locationId);
				qry.setInteger(2, locationTagAttributeId);
				qry.addEntity(LocationTagAttributeValue.class);
				
				List<LocationTagAttributeValue> list = qry.list();
				
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private LocationTagAttribute getLocationTagAttributeByName(String locationTagAttributeName) {
		try {
			String sql = "select * from chirdlutilbackports_location_tag_attribute " + "where name=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, locationTagAttributeName);
			qry.addEntity(LocationTagAttribute.class);
			
			List<LocationTagAttribute> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public LocationAttributeValue getLocationAttributeValue(Integer locationId, String locationAttributeName) {
		try {
			LocationAttribute locationAttribute = this.getLocationAttributeByName(locationAttributeName);
			
			if (locationAttribute != null) {
				Integer locationAttributeId = locationAttribute.getLocationAttributeId();
				
				String sql = "select * from chirdlutilbackports_location_attribute_value where location_id=? and location_attribute_id=?";
				SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
				
				qry.setInteger(0, locationId);
				qry.setInteger(1, locationAttributeId);
				qry.addEntity(LocationAttributeValue.class);
				
				List<LocationAttributeValue> list = qry.list();
				
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private LocationAttribute getLocationAttributeByName(String locationAttributeName) {
		try {
			String sql = "select * from chirdlutilbackports_location_attribute " + "where name=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, locationAttributeName);
			qry.addEntity(LocationAttribute.class);
			
			List<LocationAttribute> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public LocationTagAttributeValue getLocationTagAttributeValueById(Integer location_tag_attribute_value_id) {
		try {
			String sql = "select * from chirdlutilbackports_location_tag_attribute_value "
			        + "where location_tag_attribute_value_id=?";
			
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, location_tag_attribute_value_id);
			qry.addEntity(LocationTagAttributeValue.class);
			List<LocationTagAttributeValue> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public LocationTagAttribute getLocationTagAttribute(Integer locationTagAttributeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LocationTagAttribute.class).add(
		    Expression.eq("locationTagAttributeId", locationTagAttributeId));
		
		List<LocationTagAttribute> locations = criteria.list();
		if (null == locations || locations.isEmpty()) {
			return null;
		}
		return locations.get(0);
	}
	
	public LocationTagAttribute getLocationTagAttribute(String locationTagAttributeName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LocationTagAttribute.class).add(
		    Expression.eq("name", locationTagAttributeName));
		
		List<LocationTagAttribute> locations = criteria.list();
		if (null == locations || locations.isEmpty()) {
			return null;
		}
		return locations.get(0);
	}
	
	public LocationTagAttribute saveLocationTagAttribute(LocationTagAttribute value) {
		sessionFactory.getCurrentSession().saveOrUpdate(value);
		return value;
	}
	
	public LocationTagAttributeValue saveLocationTagAttributeValue(LocationTagAttributeValue value) {
		sessionFactory.getCurrentSession().saveOrUpdate(value);
		return value;
	}
	
	public LocationAttributeValue saveLocationAttributeValue(LocationAttributeValue value) {
		sessionFactory.getCurrentSession().saveOrUpdate(value);
		return value;
	}
	
	public void deleteLocationTagAttribute(LocationTagAttribute value) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LocationTagAttributeValue.class).add(
		    Expression.eq("locationTagAttributeId", value.getLocationTagAttributeId()));
		
		List<LocationTagAttributeValue> locations = criteria.list();
		if (null != locations) {
			for (LocationTagAttributeValue attr : locations) {
				deleteLocationTagAttributeValue(attr);
			}
		}
		
		sessionFactory.getCurrentSession().delete(value);
	}
	
	public void deleteLocationTagAttributeValue(LocationTagAttributeValue value) {
		sessionFactory.getCurrentSession().delete(value);
	}
	
	private Integer insertFormInstance(Integer formId, Integer locationId) {
		try {
			FormService formService = Context.getFormService();
			Form form = formService.getForm(formId);
			String formName = form.getName();
			// This is a work around since I couldn't get hibernate to create
			// a two column auto-generated key
			StatelessSession session = this.sessionFactory.openStatelessSession();
			Transaction tx = session.beginTransaction();
			Connection con = session.connection();
			int rowsInserted = 0;
			String sql = "insert into chirdlutilbackports_form_instance(form_instance_id,form_id,location_id) "
			        + " select max(form_instance_id),?,? from (select form_instance_id from  ("
			        + " select max(form_instance_id)+1 as form_instance_id,location_id "
			        + " from chirdlutilbackports_form_instance where location_id=? and form_id in "
			        + " (select form_id from form where name=?) group by location_id)a" + " union select 1 from dual)a";
			PreparedStatement stmt = null;
			try {
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, formId);
				stmt.setInt(2, locationId);
				stmt.setInt(3, locationId);
				stmt.setString(4, formName);
				
				rowsInserted = stmt.executeUpdate();
			}
			catch (Exception e) {
				this.log.error(e.getMessage());
				this.log.error(e);
			}
			finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (tx != null) {
						tx.commit();
					}
					if (session != null) {
						session.close();
					}
				}
				catch (Exception e) {
					log.error("Error generated", e);
				}
			}
			
			return rowsInserted;
			
		}
		catch (Exception e) {
			log.error(e);
		}
		return 0;
	}
	
	public synchronized FormInstance addFormInstance(Integer formId, Integer locationId) {
		PreparedStatement stmt = null;
		Transaction tx = null;
		StatelessSession session = null;
		try {
			Integer rowsInserted = insertFormInstance(formId, locationId);
			
			if (rowsInserted > 0) {
				FormService formService = Context.getFormService();
				Form form = formService.getForm(formId);
				String formName = form.getName();
				session = this.sessionFactory.openStatelessSession();
				tx = session.beginTransaction();
				Connection con = session.connection();
				String sql = "select max(form_instance_id) as form_instance_id,"
				        + "? as form_id,location_id from chirdlutilbackports_form_instance " 
				        + "inner join form f using (form_id) where location_id=? "
				        + "and f.name=? group by location_id";
				
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, formId);
				stmt.setInt(2, locationId);
				stmt.setString(3, formName);
				
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					Integer formInstanceId = rs.getInt(1);
					formId = rs.getInt(2);
					locationId = rs.getInt(3);
					FormInstance formInstance = new FormInstance(locationId, formId, formInstanceId);
					return formInstance;
				}
			}
			
		}
		catch (Exception e) {
			log.error(e);
		}
		finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (tx != null) {
					tx.commit();
				}
				if (session != null) {
					session.close();
				}
			}
			catch (Exception e) {
				log.error("Error generated", e);
			}
		}
		return null;
	}
	
	public FormAttribute getFormAttributeByName(String formAttributeName) {
		try {
			String sql = "select * from chirdlutilbackports_form_attribute where name=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, formAttributeName);
			qry.addEntity(FormAttribute.class);
			
			List<FormAttribute> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public State getStateByName(String stateName) {
		try {
			String sql = "select * from chirdlutilbackports_state where name=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, stateName);
			qry.addEntity(State.class);
			
			List<State> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public FormAttributeValue getFormAttributeValue(Integer formId, String formAttributeName, Integer locationTagId,
	                                                Integer locationId) {
		try {
			FormAttribute formAttribute = this.getFormAttributeByName(formAttributeName);
			
			if (formAttribute != null) {
				Integer formAttributeId = formAttribute.getFormAttributeId();
				
				String sql = "select * from chirdlutilbackports_form_attribute_value where form_id=? "
				        + "and form_attribute_id=? and location_tag_id=? and location_id=?";
				SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
				
				qry.setInteger(0, formId);
				qry.setInteger(1, formAttributeId);
				qry.setInteger(2, locationTagId);
				qry.setInteger(3, locationId);
				qry.addEntity(FormAttributeValue.class);
				
				List<FormAttributeValue> list = qry.list();
				
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
				
			}
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public Session getSession(int sessionId) {
		try {
			String sql = "select * from chirdlutilbackports_session where session_id=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, sessionId);
			qry.addEntity(Session.class);
			return (Session) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public List<PatientState> getPatientStatesWithForm(int sessionId) {
		try {
			String sql = "select * from chirdlutilbackports_patient_state where session_id=? and form_id is not null and retired=? order by start_time desc,end_time desc";
			SQLQuery qry = null;
			
			qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, sessionId);
			qry.setBoolean(1, false);
			qry.addEntity(PatientState.class);
			return qry.list();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public PatientState getPrevPatientStateByAction(int sessionId, int patientStateId, String action) {
		try {
			String sql = "select * from chirdlutilbackports_patient_state where session_id=? and patient_state_id < ? and retired=?"
			        + " order by patient_state_id desc";
			SQLQuery qry = null;
			qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, sessionId);
			qry.setInteger(1, patientStateId);
			qry.setBoolean(2, false);
			qry.addEntity(PatientState.class);
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
			log.error(e);
		}
		
		return null;
	}
	
	public StateMapping getStateMapping(State initialState, Program program) {
		try {
			String sql = "select * from chirdlutilbackports_state_mapping where initial_state=? and program_id=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, initialState.getStateId());
			qry.setInteger(1, program.getProgramId());
			qry.addEntity(StateMapping.class);
			return (StateMapping) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public Program getProgramByNameVersion(String name, String version) {
		try {
			String sql = "select * from chirdlutilbackports_program where name=? and version=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, name);
			qry.setString(1, version);
			qry.addEntity(Program.class);
			return (Program) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public Program getProgram(Integer programId) {
		try {
			String sql = "select * from chirdlutilbackports_program where program_id=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, programId);
			qry.addEntity(Program.class);
			return (Program) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public Session addSession(Session session) {
		try {
			this.sessionFactory.getCurrentSession().save(session);
			return session;
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public Session updateSession(Session session) {
		try {
			this.sessionFactory.getCurrentSession().save(session);
			return session;
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public PatientState addUpdatePatientState(PatientState patientState) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(patientState);		
		return patientState;
	}
	
	public StateAction getStateActionByName(String action) {
		try {
			String sql = "select * from chirdlutilbackports_state_action where action_name=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, action);
			qry.addEntity(StateAction.class);
			return (StateAction) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public List<PatientState> getPatientStateByEncounterState(Integer encounterId, Integer stateId) {
		try {
			String sql = "select a.* from chirdlutilbackports_patient_state a inner join chirdlutilbackports_session b on a.session_id=b.session_id "
			        + " where b.encounter_id=? and " + "a.state=? order by start_time desc, end_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, encounterId);
			qry.setInteger(1, stateId);
			qry.addEntity(PatientState.class);
			
			return qry.list();
		}
		catch (Exception e) {
			log.error("", e);
		}
		
		return null;
	}
	
	public List<PatientState> getPatientStateBySessionState(Integer sessionId, Integer stateId) {
		
		try {
			String sql = "select * from chirdlutilbackports_patient_state where session_id=? and state=? and retired=? order by start_time desc, end_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, sessionId);
			qry.setInteger(1, stateId);
			qry.setBoolean(2, false);
			qry.addEntity(PatientState.class);
			
			return qry.list();
			
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public List<PatientState> getPatientStatesBySession(Integer sessionId, boolean isRetired) {
		
		try {
			String sql = "select * from chirdlutilbackports_patient_state where session_id=? and retired=? order by start_time desc, end_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, sessionId);
			qry.setBoolean(1, isRetired);
			qry.addEntity(PatientState.class);
			
			return qry.list();
			
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public PatientState getPatientStateByEncounterFormAction(Integer encounterId, Integer formId, String action) {
		
		try {
			// limit to states for the session that match the form id
			String sql = "select a.* from chirdlutilbackports_patient_state a inner join chirdlutilbackports_session b on a.session_id=b.session_id "
			        + " where b.encounter_id=? " + "and a.form_id=? and a.retired=? order by start_time desc, end_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, encounterId);
			qry.setInteger(1, formId);
			qry.setBoolean(2, false);
			qry.addEntity(PatientState.class);
			
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
			log.error(e);
		}
		return null;
	}
	
	public List<PatientState> getPatientStatesByFormInstance(FormInstance formInstance, boolean isRetired) {
		
		try {
			// limit to states for the session that match the form id
			String sql = "select * from chirdlutilbackports_patient_state where form_instance_id=? "
			        + "and form_id=? and location_id=? and retired=? " + "order by start_time desc, end_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, formInstance.getFormInstanceId());
			qry.setInteger(1, formInstance.getFormId());
			qry.setInteger(2, formInstance.getLocationId());
			qry.setBoolean(3, isRetired);
			qry.addEntity(PatientState.class);
			
			return qry.list();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public PatientState getPatientStateByFormInstanceAction(FormInstance formInstance, String action) {
		
		try {
			// limit to states for the session that match the form id
			List<PatientState> states = getPatientStatesByFormInstance(formInstance, false);
			
			// return the most recent state with the given action
			for (PatientState state : states) {
				StateAction stateAction = state.getState().getAction();
				if (stateAction != null && stateAction.getActionName().equalsIgnoreCase(action)) {
					return state;
				}
			}
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state) {
		return getPatientStateByFormInstanceState(formInstance,state,false);
	}

	
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state, boolean includeRetired) {
		try {
			Integer stateId = state.getStateId();
			String retiredString = " ";
			
			if(!includeRetired){
				retiredString = " and retired=? ";
			}
			// limit to states for the session that match the form id
			String sql = "select * from chirdlutilbackports_patient_state where form_instance_id=? "
			        + "and form_id=? and location_id=?"+retiredString+"and state=? "
			        + "order by start_time desc, end_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, formInstance.getFormInstanceId());
			qry.setInteger(1, formInstance.getFormId());
			qry.setInteger(2, formInstance.getLocationId());
			if(includeRetired){
				qry.setInteger(3, stateId);
			}else{
				qry.setBoolean(3, false);
				qry.setInteger(4, stateId);
			}
			
			qry.addEntity(PatientState.class);
			
			return qry.list();
		}
		catch (Exception e) {
			log.error("", e);
		}
		
		return null;
	}
	
	public List<PatientState> getUnfinishedPatientStateByStateName(String stateName, Date optionalDateRestriction,
	                                                               Integer locationTagId, Integer locationId) {
		try {
			State state = this.getStateByName(stateName);
			String dateRestriction = "";
			if (optionalDateRestriction != null) {
				dateRestriction = " and start_time >= ?";
			}
			String sql = "select * from chirdlutilbackports_patient_state where state in "
			        + "(?) and end_time is null and retired=? and location_tag_id=? " + " and location_id=? "
			        + dateRestriction + " order by start_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, state.getStateId());
			qry.setBoolean(1, false);
			qry.setInteger(2, locationTagId);
			qry.setInteger(3, locationId);
			qry.addEntity(PatientState.class);
			if (optionalDateRestriction != null) {
				qry.setDate(3, optionalDateRestriction);
			}
			return qry.list();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public List<PatientState> getUnfinishedPatientStateByStateSession(String stateName, Integer sessionId) {
		try {
			State state = this.getStateByName(stateName);
			
			String sql = "select * from chirdlutilbackports_patient_state where state in "
			        + "(?) and end_time is null and retired=? and session_id=? " + " order by start_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, state.getStateId());
			qry.setBoolean(1, false);
			qry.setInteger(2, sessionId);
			qry.addEntity(PatientState.class);
			
			return qry.list();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public List<PatientState> getUnfinishedPatientStatesAllPatients(Date optionalDateRestriction, Integer locationTagId,
	                                                                Integer locationId) {
		try {
			String dateRestriction = "";
			if (optionalDateRestriction != null) {
				dateRestriction = " and start_time >= ?";
			}
			String sql = "select * from chirdlutilbackports_patient_state where end_time is null "
			        + "and retired=? and location_tag_id=? and location_id=?" + dateRestriction
			        + " order by start_time desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addEntity(PatientState.class);
			qry.setBoolean(0, false);
			qry.setInteger(1, locationTagId);
			qry.setInteger(2, locationId);
			if (optionalDateRestriction != null) {
				qry.setDate(3, optionalDateRestriction);
			}
			return qry.list();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public PatientState getLastUnfinishedPatientState(Integer sessionId) {
		try {
			String sql = "select * from chirdlutilbackports_patient_state where session_id=? "
			        + " and end_time is null and retired=? order by start_time desc, patient_state_id desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, sessionId);
			qry.setBoolean(1, false);
			qry.addEntity(PatientState.class);
			List<PatientState> states = qry.list();
			if (states != null && states.size() > 0) {
				return states.get(0);
			}
			return null;
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public PatientState getLastPatientState(Integer sessionId) {
		try {
			String sql = "select * from chirdlutilbackports_patient_state where session_id=? "
			        + " and retired=? order by start_time desc,end_time desc, patient_state_id desc";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, sessionId);
			qry.setBoolean(1, false);
			qry.addEntity(PatientState.class);
			List<PatientState> states = qry.list();
			if (states != null && states.size() > 0) {
				return states.get(0);
			}
			return null;
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public State getState(Integer stateId) {
		try {
			String sql = "select * from chirdlutilbackports_state where state_id=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, stateId);
			qry.addEntity(State.class);
			
			return (State) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	private List<String> getListMappedStates(Integer programId, String startStateName) {
		
		List<String> orderedStateNames = new ArrayList<String>();
		String sql = "Select * from chirdlutilbackports_state_mapping where program_id=?";
		SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		qry.addEntity(StateMapping.class);
		qry.setInteger(0, programId);
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
	public List<PatientState> getLastPatientStateAllPatients(Date optionalDateRestriction, Integer programId,
	                                                         String startStateName, Integer locationTagId, Integer locationId) {
		try {
			ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
			List<PatientState> patientStates = new ArrayList<PatientState>();
			String dateRestriction = "";
			if (optionalDateRestriction != null) {
				dateRestriction = " and start_time >= ?";
			}
			
			String sql = "select aps.* from chirdlutilbackports_patient_state aps, chirdlutilbackports_session cs, encounter e"
			        + " where aps.session_id = cs.session_id and cs.encounter_id = e.encounter_id "
			        + " and retired=? and location_tag_id=? and aps.location_id=? " + dateRestriction
			        + " order by e.encounter_datetime desc,start_time desc";
			
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			
			qry.setBoolean(0, false);
			qry.setInteger(1, locationTagId);
			qry.setInteger(2, locationId);
			if (optionalDateRestriction != null) {
				qry.setDate(3, optionalDateRestriction);
			}
			qry.addEntity(PatientState.class);
			List<PatientState> states = qry.list();
			LinkedHashMap<Integer, LinkedHashMap<String, PatientState>> patientStateMap = new LinkedHashMap<Integer, LinkedHashMap<String, PatientState>>();
			LinkedHashMap<String, PatientState> stateNameMap = null;
			for (PatientState patientState : states) {
				Integer sessionId = patientState.getSessionId();
				Integer encounterId = chirdlUtilBackportsService.getSession(sessionId).getEncounterId();
				stateNameMap = patientStateMap.get(encounterId);
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
				stateNameMap = patientStateMap.get(encounterId);
				for (int i = mappedStateNames.size() - 1; i >= 0; i--) {
					String currStateName = mappedStateNames.get(i);
					PatientState currPatientState = stateNameMap.get(currStateName);
					if (currPatientState != null) {
						patientStates.add(currPatientState);
						break;
					}
				}
			}
			return patientStates;
			
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public List<FormAttributeValue> getFormAttributesByName(String attributeName) {
		try {
			String sql = "select * from chirdlutilbackports_form_attribute_value where form_attribute_id in "
			        + "(select form_attribute_id from chirdlutilbackports_form_attribute where name=?)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addEntity(FormAttributeValue.class);
			qry.setString(0, attributeName);
			return qry.list();
			
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public ArrayList<String> getFormAttributesByNameAsString(String attributeName) {
		try {
			String sql = "select distinct value from chirdlutilbackports_form_attribute_value where form_attribute_id in "
			        + "(select form_attribute_id from chirdlutilbackports_form_attribute where name=?)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addScalar("value");
			qry.setString(0, attributeName);
			List<String> list = qry.list();
			
			ArrayList<String> exportDirectories = new ArrayList<String>();
			for (String currResult : list) {
				exportDirectories.add(currResult);
			}
			
			return exportDirectories;
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public List<State> getStatesByActionName(String actionName) {
		try {
			String sql = "select * from chirdlutilbackports_state where state_action_id="
			        + "(select state_action_id from chirdlutilbackports_state_action where action_name=?)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, actionName);
			qry.addEntity(State.class);
			return qry.list();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public PatientState getPatientState(Integer patientStateId) {
		try {
			String sql = "select * from chirdlutilbackports_patient_state where patient_state_id=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, patientStateId);
			qry.addEntity(PatientState.class);
			return (PatientState) qry.uniqueResult();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public List<PatientState> getAllRetiredPatientStatesWithForm(Date thresholdDate) {
		try {
			String sql = "select * from chirdlutilbackports_patient_state where form_id is not null and form_instance_id is not null "
			        + "and retired=? and start_time < ?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			
			qry.setBoolean(0, true);
			qry.setDate(1, thresholdDate);
			qry.addEntity(PatientState.class);
			return qry.list();
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public List<Session> getSessionsByEncounter(int encounterId) {
		try {
			String sql = "select * from chirdlutilbackports_session where encounter_id=? ";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, encounterId);
			qry.addEntity(Session.class);
			return (List<Session>) qry.list();
			
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public Program getProgram(Integer locationTagId, Integer locationId) {
		try {
			String sql = "select * from chirdlutilbackports_program_tag_map where location_id=? and location_tag_id=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, locationId);
			qry.setInteger(1, locationTagId);
			qry.addEntity(ProgramTagMap.class);
			ProgramTagMap map = (ProgramTagMap) qry.uniqueResult();
			
			if (map != null) {
				return map.getProgram();
			}
			
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public List<PatientState> getPatientStatesWithFormInstances(String formName, Integer encounterId) {
		
		SQLQuery qry = null;
		
		if (formName != null) {
			String sql = "select a.* from chirdlutilbackports_patient_state a " + "inner join chirdlutilbackports_session b on a.session_id=b.session_id "
			        + "inner join form c on a.form_id=c.form_id where " + "b.encounter_id=? and c.name=? and "
			        + "form_instance_id is not null order by end_time desc";
			qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, encounterId);
			qry.setString(1, formName);
		} else {
			String sql = "select a.* from chirdlutilbackports_patient_state a " + "inner join chirdlutilbackports_session b on a.session_id=b.session_id "
			        + "where b.encounter_id=? and " + "form_instance_id is not null order by end_time desc";
			qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setInteger(0, encounterId);
		}
		
		qry.addEntity(PatientState.class);
		return qry.list();
	}
	
	public void saveError(Error error) {
		try {
			this.sessionFactory.getCurrentSession().save(error);
		}
		catch (Exception e) {
			this.log.error(e);
		}
	}
	
	public List<Error> getErrorsByLevel(String errorLevel, Integer sessionId) {
		try {
			String sql = "select * from chirdlutilbackports_error where level=? and session_id=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, errorLevel);
			qry.setInteger(1, sessionId);
			qry.addEntity(Error.class);
			return qry.list();
		}
		catch (Exception e) {
			this.log.error(e);
		}
		return null;
	}
	
	public List<FormAttributeValue> getFormAttributeValuesByValue(String value) {
		try {
			String sql = "select * from chirdlutilbackports_form_attribute_value where value=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, value);
			qry.addEntity(FormAttributeValue.class);
			return qry.list();
		}
		catch (Exception e) {
			this.log.error(e);
		}
		return null;
	}
	
	public void saveFormAttributeValue(FormAttributeValue value) {
		sessionFactory.getCurrentSession().saveOrUpdate(value);
	}
	
	public Integer getErrorCategoryIdByName(String name) {
		try {
			Connection con = this.sessionFactory.getCurrentSession().connection();
			String sql = "select error_category_id from chirdlutilbackports_error_category where name=?";
			try {
				PreparedStatement stmt = con.prepareStatement(sql);
				stmt.setString(1, name);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					return rs.getInt(1);
				}
				
			}
			catch (Exception e) {

			}
			return null;
		}
		catch (Exception e) {
			this.log.error(e);
		}
		return null;
	}

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getObsAttributeByName(java.lang.String)
	 */
    public ObsAttribute getObsAttributeByName(String obsAttributeName) {
		try {
			String sql = "select * from chirdlutilbackports_obs_attribute where name=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, obsAttributeName);
			qry.addEntity(ObsAttribute.class);
			
			List<ObsAttribute> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getObsAttributesByName(java.lang.String)
	 */
    public List<ObsAttributeValue> getObsAttributesByName(String attributeName) {
    	try {
			String sql = "select * from chirdlutilbackports_obs_attribute_value where obs_attribute_id in "
			        + "(select obs_attribute_id from chirdlutilbackports_obs_attribute where name=?)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addEntity(ObsAttributeValue.class);
			qry.setString(0, attributeName);
			return qry.list();
			
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getObsAttributesByNameAsString(java.lang.String)
	 */
    public List<String> getObsAttributesByNameAsString(String attributeName) {
    	try {
			String sql = "select distinct value from chirdlutilbackports_obs_attribute_value where obs_attribute_id in "
			        + "(select obs_attribute_id from chirdlutilbackports_obs_attribute where name=?)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addScalar("value");
			qry.setString(0, attributeName);
			List<String> list = qry.list();
			
			ArrayList<String> attributes = new ArrayList<String>();
			for (String currResult : list) {
				attributes.add(currResult);
			}
			
			return attributes;
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getObsAttributeValue(java.lang.Integer, java.lang.String)
	 */
    public ObsAttributeValue getObsAttributeValue(Integer obsId, String obsAttributeName) {
    	try {
			ObsAttribute obsAttribute = this.getObsAttributeByName(obsAttributeName);
			
			if (obsAttribute != null) {
				Integer obsAttributeId = obsAttribute.getObsAttributeId();
				
				String sql = "select * from chirdlutilbackports_obs_attribute_value where obs_id=? "
				        + "and obs_attribute_id=?";
				SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
				
				qry.setInteger(0, obsId);
				qry.setInteger(1, obsAttributeId);
				qry.addEntity(ObsAttributeValue.class);
				
				List<ObsAttributeValue> list = qry.list();
				
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
				
			}
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getObsAttributeValuesByValue(java.lang.String)
	 */
    public List<ObsAttributeValue> getObsAttributeValuesByValue(String value) {
    	try {
			String sql = "select * from chirdlutilbackports_obs_attribute_value where value=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, value);
			qry.addEntity(ObsAttributeValue.class);
			return qry.list();
		}
		catch (Exception e) {
			this.log.error(e);
		}
		return null;
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#saveObsAttributeValue(org.openmrs.module.chirdlutilbackports.hibernateBeans.ObsAttributeValue)
	 */
    public void saveObsAttributeValue(ObsAttributeValue value) {
    	sessionFactory.getCurrentSession().saveOrUpdate(value);
    }

	@Override
    public FormInstanceAttribute getFormInstanceAttributeByName(String formInstanceAttributeName) {
		try {
			String sql = "select * from chirdlutilbackports_form_instance_attribute where name=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, formInstanceAttributeName);
			qry.addEntity(FormInstanceAttribute.class);
			
			List<FormInstanceAttribute> list = qry.list();
			
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
    }

	@Override
    public List<FormInstanceAttributeValue> getFormInstanceAttributesByName(String attributeName) {
		try {
			String sql = "select * from chirdlutilbackports_form_instance_attribute_value where "
					+ "form_instance_attribute_id in "
			        + "(select form_instance_attribute_id from chirdlutilbackports_form_instance_attribute where name=?)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addEntity(FormInstanceAttributeValue.class);
			qry.setString(0, attributeName);
			return qry.list();
			
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
    }

	@Override
    public List<String> getFormInstanceAttributesByNameAsString(String attributeName) {
		try {
			String sql = "select distinct value from chirdlutilbackports_form_instance_attribute_value where "
					+ "form_instance_attribute_id in "
			        + "(select form_instance_attribute_id from chirdlutilbackports_form_instance_attribute where name=?)";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.addScalar("value");
			qry.setString(0, attributeName);
			List<String> list = qry.list();
			
			ArrayList<String> attributes = new ArrayList<String>();
			for (String currResult : list) {
				attributes.add(currResult);
			}
			
			return attributes;
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
    }

	@Override
    public FormInstanceAttributeValue getFormInstanceAttributeValue(Integer formId, Integer formInstanceId,
                                                                    Integer locationId, String formInstanceAttributeName) {
		try {
			FormInstanceAttribute formInstanceAttribute = this.getFormInstanceAttributeByName(formInstanceAttributeName);
			
			if (formInstanceAttribute != null) {
				Integer formInstanceAttributeId = formInstanceAttribute.getFormInstanceAttributeId();
				
				String sql = "select * from chirdlutilbackports_form_instance_attribute_value where form_id=? "
				        + "and form_instance_id=? and location_id=? and form_instance_attribute_id=?";
				SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
				
				qry.setInteger(0, formId);
				qry.setInteger(1, formInstanceId);
				qry.setInteger(2, locationId);
				qry.setInteger(3, formInstanceAttributeId);
				qry.addEntity(FormInstanceAttributeValue.class);
				
				List<FormInstanceAttributeValue> list = qry.list();
				
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
				
			}
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
    }

	@Override
    public List<FormInstanceAttributeValue> getFormInstanceAttributeValuesByValue(String value) {
		try {
			String sql = "select * from chirdlutilbackports_form_instance_attribute_value where value=?";
			SQLQuery qry = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			qry.setString(0, value);
			qry.addEntity(FormInstanceAttributeValue.class);
			return qry.list();
		}
		catch (Exception e) {
			this.log.error(e);
		}
		return null;
    }

	@Override
    public void saveFormInstanceAttributeValue(FormInstanceAttributeValue value) {
		sessionFactory.getCurrentSession().saveOrUpdate(value);
    }
}
