package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.crimes.model.District;
import it.polito.tdp.crimes.model.Event;



public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	
	public List<Integer> getAnni(){
		
		
		String sql = "SELECT DISTINCT YEAR(reported_date) AS anno "
				+ "FROM events ";
		

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer>anni=new LinkedList<Integer>();
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				anni.add(res.getInt("anno"));
				
				
			}
			
			Collections.sort(anni);
			
			
			conn.close();
			return anni ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		
	}
	
	
	
	public void loadDistretti(Map<Integer, District>idMap) {
		
		
		String sql = "SELECT DISTINCT district_id "
				+ "FROM events ";

		

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				District d=new District(res.getInt("district_id"));
				idMap.put(res.getInt("district_id"), d);
				
				
			}
			
		
			
			
			conn.close();
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
		
		
		
		
	}
	
	
	//per ogni distretto determinare la media della latitudine dei suoi crimini nell'anno selezionato
	
	public double getAvgLat(District d, int anno){
		
		double avg=0.0;
		
		String sql = "SELECT AVG(geo_lat) "
				+ "FROM EVENTS "
				+ "WHERE district_id=? AND YEAR(reported_date)= ? ";
		

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, d.getDistrinct_id());
			st.setInt(2, anno);
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				
				avg=res.getDouble("AVG(geo_lat)");
				
			}
			
		
			
			
			conn.close();
			return avg;
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0.0;
		}
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	//per ogni distretto determinare la media della longitudine dei suoi crimini nell'anno selezionato
	
public double getAvgLon(District d, int anno){
		
		double avg=0.0;
		
		String sql = "SELECT AVG(geo_lon) "
				+ "FROM EVENTS "
				+ "WHERE district_id= ? AND YEAR(reported_date)= ? ";
		

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, d.getDistrinct_id());
			st.setInt(2, anno);
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				
				avg=res.getDouble("AVG(geo_lon)");
				
			}
			
		
			
			
			conn.close();
			return avg;
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0.0;
		}
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
}
