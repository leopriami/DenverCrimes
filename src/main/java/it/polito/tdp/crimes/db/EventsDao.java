package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
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
	
	public List<String> listCategories(){
		String sql = "SELECT DISTINCT offense_category_id FROM events order by offense_category_id asc" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getString("offense_category_id"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println("errore dao categorie");
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
	
	public List<Integer> listMonths(){
		String sql = "SELECT distinct month(reported_date) as m FROM denver_crimes.events order by m asc" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getInt("m"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println("errore dao mesi");
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
	
	public List<String> listTypes(String category, int month){
		String sql = "SELECT distinct offense_type_id FROM denver_crimes.events where offense_category_id=? and month(reported_date)=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, category);
			st.setInt(2, month);
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getString("offense_type_id"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println("errore dao reati");
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
	
	public List<Adiacenza> getAdiacenze(String categoria, Integer mese) {
		String sql = "select e1.offense_type_id as v1, e2.offense_type_id as v2, COUNT(DISTINCT(e1.neighborhood_id)) as peso " + 
				"from events e1, events e2 " + 
				"where e1.offense_category_id = ? " + 
				"	and e2.offense_category_id = ? " + 
				"	and Month(e1.reported_date) = ? " + 
				"	and Month(e2.reported_date) = ? " + 
				"	and e1.offense_type_id != e2.offense_type_id " + 
				"	and e1.neighborhood_id = e2.neighborhood_id " + 
				"group by e1.offense_type_id, e2.offense_type_id";
		List<Adiacenza> adiacenze = new LinkedList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setString(2, categoria);
			st.setInt(3, mese);
			st.setInt(4, mese);
			
			ResultSet res = st.executeQuery() ;
			while (res.next()) {
				adiacenze.add(new Adiacenza(res.getString("v1"), res.getString("v2"), res.getDouble("peso")));
			}
			conn.close();
			return adiacenze;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}

}
