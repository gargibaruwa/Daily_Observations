import java.sql.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.io.Console;
import java.util.Date;


public class gbaruwa {
	
    static final String jdbcURL = "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl";
    
    public static void main(String[] args) throws IOException  {
    	
    	try
    	{   	
//                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
//                
//                System.out.print("Enter username for connection :");
//                String user = br1.readLine();
//                
//                System.out.print("Enter password for connection :");
//                String passwd = br1.readLine();
//            
                String user = "gbaruwa";  // For example, "jsmith"
                String passwd = "*********";       // Your 9 digit student ID number

                Class.forName("oracle.jdbc.driver.OracleDriver");

                Connection conn = null;
                Statement stmt = null;
                ResultSet rs = null;
    	  	try{
    	  	
    	  	conn = DriverManager.getConnection(jdbcURL, user, passwd);
       		stmt = conn.createStatement();
       		
	    	while(true)
	    	{
	    		
	    		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    	   	System.out.print("Enter 1:Login 2:Create User 3:Exit:");
		    	int user_input = Integer.parseInt(br.readLine());
		        if(user_input==1)
		        {
		        	System.out.print("Enter Username (case sensitive) :");
		        	String username = br.readLine();
		        
	
				System.out.print("Enter Password (case sensitive) :");
		        	//Console console = System.console();
			        //char[] passwordChars = console.readPassword();
			        //String password = new String(passwordChars);	
		        	String password = br.readLine();
		        	// 0 - if do not match
		        	// 1 - if physician
		        	// 2 - if patient
		        	
		        	int match = 0;
		     		
		        	rs = stmt.executeQuery("select IS_PATIENT from USER_LOGIN where USER_ID = '" + username + "' and PASSWORD = '"
		        			+ password + "'" );
		        	
		        	if(rs.next())
		        	{
					//rs.next();
		        		String is_patient = rs.getString("IS_PATIENT");
					//System.out.println("asd "+is_patient);
		        		if(is_patient.equals("0"))
		        		{
		        			match = 1;
		        		}
		        		else if(is_patient.equals("1"))
		        		{
		        			match = 2;
		        		}
		        		
		        	}
		        	else
		        	{
		        		System.out.println("Login Denied, Username and Password do not match!!");
		        	}

				if(match==1)
		        	{
		        		//inside doctor
		        		while(true)
		        		{
		        		
			        		System.out.print("Enter 1:Add a New Observation Type 2:Add an Association Between Observation Type and" +
			        			 " Illness \n 3:View Patients 4:Assign Patient to a specific Illness Class 5:Back:");
			        		int phyisican_input = Integer.parseInt(br.readLine());
			        		if(phyisican_input==1)
			        		{
			        			//add a new observation type
			        			System.out.print("Enter Observation Type name:");
			        			String new_obv_type = br.readLine();
			        			
                                                	System.out.println("Enter Observation Category ID:");
                                                        rs = stmt.executeQuery("select CATEGORY_ID,CATEGORY_NAME from OBSERVATION_CATEGORY");
                                                        
                                                        while(rs.next())
                                                        {
                                                             System.out.println(rs.getString("CATEGORY_ID") + ":" + rs.getString("CATEGORY_NAME") );
                                                        }
                                                        
			        			int obv_cat = Integer.parseInt(br.readLine());
			        			
			        			//enter attribute names
			        			System.out.print("Enter Observation Type attribute 1:");
			        			String new_obv_att1 = br.readLine();
			        			
			        			System.out.print("Enter Observation Type attribute 2, if not enter NA:");
			        			String new_obv_att2 = br.readLine();
                                                        if(new_obv_att2.equals("NA"))
                                                        {
                                                            new_obv_att2 = "";
                                                        }
                                                        
                                                        System.out.print("Enter Threshold value, if applicable,else NA:");
			        			String threshold = br.readLine();
                                                        if(threshold.equals("NA"))
                                                        {
                                                            threshold = "";
                                                        }
			        			
			        			//display all the illness with their ids.
			        			System.out.println("Enter Disease class id:");
			        			//sql code to display all the diseases.
                                                        rs = stmt.executeQuery("select CLASS_ID,CLASS_NAME from DISEASE_CLASS");
                                                        while(rs.next())
                                                        {
                                                            int class_id = rs.getInt("CLASS_ID");
                                                            String class_name = rs.getString("CLASS_NAME");
                                                            System.out.println(class_id+":"+class_name);
                                                        }
			        			int disease_id = Integer.parseInt(br.readLine());
			        			
			        			System.out.println("Press 1 to confirm, else 0");
			        			int choice = Integer.parseInt(br.readLine());
			        			
			        			if(choice==1)
			        			{
                                                            rs = stmt.executeQuery("select * from OBSERVATION_TYPE B where lower(B.TYPE_NAME)= '" + new_obv_type.toLowerCase() + "'");
                                                            
                                                            if(rs.next())
                                                            {
					
                                                                System.out.println("The observation type " + new_obv_type + " already exists!!");
	        		
                                                            }
                                                            else
                                                            {
                                                                //insert row in observation type
                                                                //System.out.println("insert into OBSERVATION_TYPE(TYPE_NAME,THRESHOLD_VAL,ADDITIONAL_INFO_1,ADDITIONAL_INFO_2) VALUES ('"   + new_obv_type + "','" + threshold + "','" + new_obv_att1 + "','" + new_obv_att2 + "')" );
                                                                 int execute1 = stmt.executeUpdate("insert into OBSERVATION_TYPE(TYPE_NAME,THRESHOLD_VAL,ADDITIONAL_INFO_1,ADDITIONAL_INFO_2) VALUES ('"   + new_obv_type + "','" + threshold + "','" + new_obv_att1 + "','" + new_obv_att2 + "')" );
                                                                 

                                                                 //use the new obv type id and cat id to insert a tuple in table OBSERVATION_TYPE_CATEGORY
                                                                 
                                                                 rs = stmt.executeQuery("select TYPE_ID from OBSERVATION_TYPE where TYPE_NAME = '" + new_obv_type +"'");
                                                                 int obv_type_id = 0;
                                                                 while(rs.next())
                                                                 {
                                                                    obv_type_id = rs.getInt("TYPE_ID");
                                                                 }
                                                                 //System.out.println("insert into OBSERVATION_TYPE_CATEGORY VALUES (" + obv_cat + "," + obv_type_id + ")");
                                                                 int execute2 = stmt.executeUpdate("insert into OBSERVATION_TYPE_CATEGORY VALUES (" + obv_cat + "," + obv_type_id + ")");
                                                                 
                                                                 // use new type id and disease id to insert a tuple in DISEASE_OBSERVATION_TYPE
                                                                 int execute3 = stmt.executeUpdate("insert into DISEASE_OBSERVATION_TYPE VALUES (" + disease_id + "," + obv_type_id +")" );

                                                                 if(execute1==1 && execute2==1 && execute3==1)
                                                                 {
                                                                     stmt.executeUpdate("COMMIT");
                                                                     System.out.println("New Observation type inserted");
                                                                 }
                                                                 else
                                                                 {
                                                                    stmt.executeUpdate("ROLLBACK");
                                                                    System.out.println("New Observation type could not be inserted");
                                                                 }
                                 
                                                            }

                               	}
			        			else
			        			{
			        				//do nothing
			        			}
			        	    			
			        		}
			        		else if(phyisican_input==2)
			        		{
			        			//association b/w type and disease
                                                        System.out.print("Enter Disease class id:");
			        			//sql code to display all the diseases.
                                                        rs = stmt.executeQuery("select CLASS_ID,CLASS_NAME from DISEASE_CLASS");
                                                        while(rs.next())
                                                        {
                                                            int class_id = rs.getInt("CLASS_ID");
                                                            String class_name = rs.getString("CLASS_NAME");
                                                            System.out.println(class_id+":"+class_name);
                                                        }
			        			int class_id = Integer.parseInt(br.readLine());
                                                        
                                                        
			        			System.out.print("Enter Observation Type id:");
			        			//display all the available observation types
                                                        rs = stmt.executeQuery("select TYPE_ID,TYPE_NAME from OBSERVATION_TYPE");
                                                        while(rs.next())
                                                        {
                                                            int TYPE_ID = rs.getInt("TYPE_ID");
                                                            String TYPE_NAME = rs.getString("TYPE_NAME");
                                                            System.out.println(TYPE_ID+":"+TYPE_NAME);
                                                        }   
                                                            
			        			int new_obv_id = Integer.parseInt(br.readLine());
			        			
			        			//first check if the association already exists or not, if not insert
			        			// the details into the table DISEASE_OBSERVATION_TYPE
                                                        rs = stmt.executeQuery("select * from DISEASE_OBSERVATION_TYPE where CLASS_ID = " + class_id + " and TYPE_ID = " + new_obv_id + "");
							if(!rs.next())
							{
                                                           
                                                            int cnt1 = -1;
                                                            int execute = -1;                                                            
                                                            rs = stmt.executeQuery("select count(*) as COUNT from DISEASE_OBSERVATION_TYPE d, DISEASE_CLASS c where d.CLASS_ID = c.CLASS_ID and lower(CLASS_NAME) = 'general' and TYPE_ID =" + new_obv_id);
                                                            while(rs.next())
                                                            {
                                                                cnt1 = rs.getInt("COUNT");
                                                            }
                                                            
                                                            if(cnt1 == -1)
                                                            {
                                                                System.out.println("Something is wrong!!");
                                                            }
                                                            else if(cnt1 == 1)
                                                            {
                                                                execute = stmt.executeUpdate("update DISEASE_OBSERVATION_TYPE set CLASS_ID =" + class_id + " where TYPE_ID =" + new_obv_id );
                                                            }
                                                            else if(cnt1 == 0)
                                                            {
                                                               execute = stmt.executeUpdate("insert into DISEASE_OBSERVATION_TYPE VALUES (" + class_id + "," + new_obv_id +")" );
                                                            }
                                                             
                                                         
                                                            if(execute==1)
                                                            {
                                                                stmt.executeUpdate("COMMIT");
                                                                System.out.println("New association inserted inserted");
                                                            }
                                                            else
                                                            {
                                                                stmt.executeUpdate("ROLLBACK");
                                                                System.out.println("New Observation type could not be inserted");
                                                            }
                                                        }
                                                        else
                                                        {
                                                            System.out.println("The Association already exists");
                                                        }
                                                        
			        			
			        		}
			        		else if(phyisican_input==3)
			        		{
			        			//view patients
			        			while(true)
			        			{		        			
				        			System.out.print("Enter View Patients: 1.View by Observation Type   2.View by Patient Name   3.View aggregated reports   4.Back:");
				        			int view = Integer.parseInt(br.readLine());
				        			
				        			if(view==1)
				        			{
				        				//view by obsv type
				        				//select all the avialable obsv type.
				        				while(true)
				        				{
				        				
					        				System.out.print("Enter Observation Type id, 0 - Back :");
                                                                                
                                                                                //display all the available observation types
                                                                                rs = stmt.executeQuery("select TYPE_ID,TYPE_NAME from OBSERVATION_TYPE");
                                                                                 while(rs.next())
                                                                                  {
                                                                                      int TYPE_ID = rs.getInt("TYPE_ID");
                                                                                      String TYPE_NAME = rs.getString("TYPE_NAME");
                                                                                      System.out.println(TYPE_ID+":"+TYPE_NAME);
                                                                                   } 
						        			//display all the available observation types
						        			int new_obv_id = Integer.parseInt(br.readLine());
						        			
						        			if(new_obv_id==0)
						        			{
						        				break;
						        			}
						        			else
						        			{
						        				//display all the observation which are his patients for the specific observation type.
                                                                                        //run below query after physician assigns disease class to patient
                                                                                        //rs = stmt.executeQuery("select OL.OBSERVATION_ID, P.PATIENT_NAME, OT.TYPE_NAME, OL.VALUE_1, OL.VALUE_2, OL.OBSERVATION_DATETIME, OL.RECORDED_DATETIME FROM OBSERVATIONS_LOG OL, OBSERVATION_TYPE OT, PATIENT P, ASSIGN_DISEASE AD WHERE AD.PHYSICIAN_ID= '" + username + "' AND AD.PATIENT_ID=P.PATIENT_ID AND P.PATIENT_ID=OL.PATIENT_ID AND OL.OBSERVATION_TYPE_ID=OT.TYPE_ID AND OT.TYPE_ID= " + new_obv_id + " ORDER BY OL.OBSERVATION_ID");
                                                                                        
                                                                                        rs = stmt.executeQuery("select OL.OBSERVATION_ID, P.PATIENT_NAME, OT.TYPE_NAME, OL.VALUE_1, case when OL.VALUE_2 is null then 'NA' else OL.VALUE_2 end as VALUE_2, OL.OBSERVATION_DATETIME, OL.RECORDED_DATETIME FROM OBSERVATIONS_LOG OL, OBSERVATION_TYPE OT, PATIENT P "
																												+"WHERE P.PATIENT_ID=OL.PATIENT_ID AND OL.OBSERVATION_TYPE_ID=OT.TYPE_ID AND OT.TYPE_ID= " + new_obv_id + " ORDER BY OL.OBSERVATION_ID");
                                                                                        
                                                                                        System.out.printf("%14s   %12s   %14s   %8s   %8s   %21s   %21s%n", "OBSERVATION_ID", "PATIENT_NAME", "TYPE_NAME", "AMOUNT_1", "AMOUNT_2", "OBSERVATION_DATETIME","RECORDED_DATETIME");
                                                                                        
                                                                                        while(rs.next())
                                                                                         {
                                                                                            int OBS_ID = rs.getInt("OBSERVATION_ID");                                                                                            
                                                                                            
                                                                                            String PATIENT_NAME = rs.getString("PATIENT_NAME");
                                                                                            
                                                                                            String TYPE_NAME = rs.getString("TYPE_NAME");
                                                                                            
                                                                                            String VALUE_1 = rs.getString("VALUE_1");
                                                                                            String VALUE_2 = rs.getString("VALUE_2"); 
                                                                                            
                                                                                            java.sql.Timestamp OBS_DATETIME = rs.getTimestamp("OBSERVATION_DATETIME");
                                                                                            String OBS_DATETIME_STR = OBS_DATETIME.toString();
                                                                                                    
                                                                                            java.sql.Timestamp REC_DATETIME = rs.getTimestamp("RECORDED_DATETIME");
                                                                                            String REC_DATETIME_STR = REC_DATETIME.toString();                                                                                
                                                                                            
                                                                                            
                                                                                            System.out.printf("%14d   %12s   %14s   %8s   %8s   %21s   %21s%n", OBS_ID, PATIENT_NAME, TYPE_NAME, VALUE_1, VALUE_2, OBS_DATETIME_STR, REC_DATETIME_STR);                                                                                            
                                                                                                                                                                                                                                                                                   
                                                                                         }
                                                                                        
						        			}
						        			
					        			}
				        			}
				        			else if(view==2)
				        			{
				        				//view by patient name
				        				while(true)
				        				{
				        					System.out.print("Enter 1.Patient name 2. Back:");
				        					int view_choice = Integer.parseInt(br.readLine());
				        					
				        					if(view_choice==1)
				        					{
				        					
                                                                                        while(true)
                                                                                        {
                                                                                            //physician needs to enter full name of patient
                                                                                            System.out.print("Enter Patient name (full name) :");
                                                                                            String patient_name = br.readLine();
                                                                                        
                                                                                            //check if patient name is valid
                                                                                            rs = stmt.executeQuery("select * from PATIENT where lower(PATIENT_NAME) = '" + patient_name.toLowerCase() + "'" );
                                                                                         
                                                                                            if(rs.next())
                                                                                            {
                                                                                                //retrieve all the observations for that particular patient using %patient_name%
                                                                                                //and order the data by obsv type.
                                                                                                rs = stmt.executeQuery("select OT.TYPE_NAME, OL.VALUE_1, case when OL.VALUE_2 is null then 'NA' else OL.VALUE_2 end as VALUE_2, OL.OBSERVATION_DATETIME, OL.RECORDED_DATETIME FROM OBSERVATIONS_LOG OL, OBSERVATION_TYPE OT, PATIENT P "
                                                                                                                        +"WHERE lower(P.PATIENT_NAME) = '" + patient_name.toLowerCase() + "' AND P.PATIENT_ID=OL.PATIENT_ID AND OL.OBSERVATION_TYPE_ID=OT.TYPE_ID GROUP BY OT.TYPE_NAME, OL.VALUE_1, OL.VALUE_2, OL.OBSERVATION_DATETIME, OL.RECORDED_DATETIME ORDER BY OL.OBSERVATION_DATETIME, OL.RECORDED_DATETIME");
                                                                                                
                                                                                                //System.out.println("OBSERVATION_TYPE    VALUE_1    VALUE_2    OBSERVATION_DATETIME    RECORDED_DATETIME");
                                                                                                //System.out.println("----------------------------------------------------------------------------------------"); 
                                                                                                System.out.printf("%1s  %-7s   %-7s   %21s   %21s%n", "OBSERVATION_TYPE", "VALUE_1", "VALUE_2", "OBSERVATION_DATETIME", "RECORDED_DATETIME");

                                                                                               
                                                                                                    
                                                                                                while(rs.next())
                                                                                                 {
                                                                                                    java.sql.Timestamp OBS_DATETIME = rs.getTimestamp("OBSERVATION_DATETIME");
                                                                                                    String OBS_DATETIME_STR = OBS_DATETIME.toString();
                                                                                                    
                                                                                                    java.sql.Timestamp REC_DATETIME = rs.getTimestamp("RECORDED_DATETIME");
                                                                                                    String REC_DATETIME_STR = REC_DATETIME.toString();
                                                                                                    
                                                                                                    String TYPE_NAME = rs.getString("TYPE_NAME");
                                                                                                    String VALUE_1 = rs.getString("VALUE_1");
                                                                                                    String VALUE_2 = rs.getString("VALUE_2");    
                                                                                                    //System.out.println(TYPE_NAME+"\t"+VALUE_1+"\t"+VALUE_2+"\t"+OBS_DATETIME+"\t"+REC_DATETIME);                                                                               
                                                                                                    System.out.printf("%16s  %7s   %7s   %21s   %21s%n", TYPE_NAME, VALUE_1, VALUE_2, OBS_DATETIME_STR, REC_DATETIME_STR);
                                                                                                 }
                                                                                                
                                                                                                break;
                                                                                            }
                                                                                            else
                                                                                            {
                                                                                                System.out.println("Invalid patient name");
                                                                                                break;
                                                                                            }
                                                                                        
                                                                                        }
					        					
				        					}
				        					else
				        					{
				        						break;
				        					}
				        					
				        				}
				        			}
                                                                
                                                                else if(view==3)
				        			{
                                                                    //view aggregated reports
                                                                    while(true)
                                                                    {
                                                                        //physician needs to enter full name of patient
                                                                        System.out.print("1-Enter a disease class id    0-Back : ");
                                                                        int doc_choice = Integer.parseInt(br.readLine());

                                                                        if(doc_choice==1)
                                                                        {
                                                                            rs=stmt.executeQuery("select CLASS_ID, CLASS_NAME from DISEASE_CLASS");

                                                                            while(rs.next())
                                                                            {
                                                                                System.out.println(rs.getString("CLASS_ID")+":"+rs.getString("CLASS_NAME"));
                                                                            }

                                                                            System.out.print("Disease class id : ");
                                                                            int class_id = Integer.parseInt(br.readLine());
                                                                            
                                                                            while(true)
                                                                            {
                                                                                System.out.print("1-Enter Observation type   0-Back : ");
                                                                                int doctor_choice = Integer.parseInt(br.readLine());

                                                                                if(doctor_choice==1)
                                                                                {
                                                                                   /* rs = stmt.executeQuery("select OT.TYPE_ID,OT.TYPE_NAME from "+
                                                                                                            "OBSERVATION_TYPE OT, DISEASE_OBSERVATION_TYPE DOT, DISEASE_CLASS DC "+
                                                                                                            "where OT.TYPE_ID=DOT.TYPE_ID "+
                                                                                                            "and DC.CLASS_ID=DOT.CLASS_ID "+
                                                                                                            "and DOT.CLASS_ID = "+class_id+
                                                                                                            " and LOWER(DC.CLASS_NAME) <> 'general' "+
                                                                                                            "UNION "+
                                                                                                            "select OT.TYPE_ID,OT.TYPE_NAME from "+
                                                                                                            "OBSERVATION_TYPE OT, DISEASE_OBSERVATION_TYPE DOT, DISEASE_CLASS DC "+
                                                                                                            "where OT.TYPE_ID=DOT.TYPE_ID "+
                                                                                                            "and DC.CLASS_ID=DOT.CLASS_ID "+
                                                                                                            "and LOWER(DC.CLASS_NAME) = 'general' "+
                                                                                                            "and lower(OT.TYPE_NAME) <> 'diet'"); */
                                                                                    
                                                                                    rs = stmt.executeQuery("select OT.TYPE_ID,OT.TYPE_NAME from "+
                                                                                                            "OBSERVATION_TYPE OT, DISEASE_OBSERVATION_TYPE DOT, DISEASE_CLASS DC "+
                                                                                                            "where OT.TYPE_ID=DOT.TYPE_ID "+
                                                                                                            "and DC.CLASS_ID=DOT.CLASS_ID "+
                                                                                                            "and DOT.CLASS_ID = "+class_id+
                                                                                                            " and lower(OT.TYPE_NAME) <> 'diet'");

                                                                                    while(rs.next())
                                                                                    {
                                                                                        System.out.println(rs.getString("TYPE_ID")+":"+rs.getString("TYPE_NAME"));
                                                                                    }

                                                                                    System.out.print("Observation type id : ");
                                                                                    int type_id = Integer.parseInt(br.readLine());
                                                                                    
                                                                                    System.out.print("Enter Start date to view reports. Date format - DD/MM/YYYY :");
                                                                                    String obs_start_dt = br.readLine();
                                                                                    
                                                                                    System.out.print("Enter End date view reports. Date format - DD/MM/YYYY :");
                                                                                    String obs_end_dt = br.readLine();
                                                                                                                                                                        
                                                                                    rs = stmt.executeQuery("select lower(TYPE_NAME) as TYPE_NAME from OBSERVATION_TYPE where TYPE_ID = "+type_id);
                                                                                    
                                                                                    String type_name="";
                                                                                    while(rs.next())
                                                                                    {
                                                                                        type_name = rs.getString("TYPE_NAME");
                                                                                    }
                                                                                                                                                                       
                                                                                    if(type_name.equals("exercise"))
                                                                                    {
                                                                                        rs = stmt.executeQuery("select OL.VALUE_1 as VAL1,round(avg(to_number(OL.VALUE_2)),3) as VAL2 "+
                                                                                                                "from OBSERVATIONS_LOG OL, ASSIGN_DISEASE AD "+
                                                                                                                "WHERE OL.PATIENT_ID = AD.PATIENT_ID "+
                                                                                                                "and OL.OBSERVATION_TYPE_ID = "+type_id+
                                                                                                                " and AD.CLASS_ID = "+class_id+
                                                                                                                " and OL.OBSERVATION_DATETIME between to_timestamp('"+obs_start_dt+"','DD/MM/YYYY') and to_timestamp('"+obs_end_dt+"','DD/MM/YYYY')"+
                                                                                                                " group by OL.VALUE_1");
                                                                                        
                                                                                        System.out.printf("%15s   %8s%n",type_name.toUpperCase(),"Duration");
                                                                                        
                                                                                        while(rs.next())
                                                                                        {
                                                                                            System.out.printf("%15s   %8s%n",rs.getString("VAL1"),rs.getString("VAL2"));
                                                                                        }
                                                                                        
                                                                                    }
                                                                                    else if(type_name.equals("mood"))
                                                                                    {
                                                                                        rs = stmt.executeQuery("select OL.VALUE_1 as VAL1, count(*) as VAL2 "+
                                                                                                                "from OBSERVATIONS_LOG OL, ASSIGN_DISEASE AD "+
                                                                                                                "WHERE OL.PATIENT_ID = AD.PATIENT_ID "+
                                                                                                                "and OL.OBSERVATION_TYPE_ID = "+type_id+
                                                                                                                " and AD.CLASS_ID = "+class_id+
                                                                                                                " and OL.OBSERVATION_DATETIME between to_timestamp('"+obs_start_dt+"','DD/MM/YYYY') and to_timestamp('"+obs_end_dt+"','DD/MM/YYYY')"+
                                                                                                                " group by OL.VALUE_1");
                                                                                        
                                                                                        System.out.printf("%15s   %8s%n",type_name.toUpperCase(),"Count");
                                                                                        
                                                                                        while(rs.next())
                                                                                        {
                                                                                            System.out.printf("%15s   %8s%n",rs.getString("VAL1"),rs.getString("VAL2"));
                                                                                        }
                                                                                        
                                                                                    }
                                                                                    else if(type_name.equals("weight") || type_name.equals("temperature") || type_name.equals("exercise tolerance") || type_name.equals("oxygen saturation") || type_name.equals("pain") || type_name.equals("contraction"))
                                                                                    {
                                                                                        rs = stmt.executeQuery("select round(avg(to_number(OL.VALUE_1)),3) as VAL1 "+
                                                                                                                "from OBSERVATIONS_LOG OL, ASSIGN_DISEASE AD "+
                                                                                                                "WHERE OL.PATIENT_ID = AD.PATIENT_ID "+
                                                                                                                "and OL.OBSERVATION_TYPE_ID = "+type_id+
                                                                                                                " and AD.CLASS_ID = "+class_id+ 
                                                                                                                " and OL.OBSERVATION_DATETIME between to_timestamp('"+obs_start_dt+"','DD/MM/YYYY') and to_timestamp('"+obs_end_dt+"','DD/MM/YYYY')");
                                                                                                                                                                                                       
                                                                                        System.out.printf("%20s%n","AVERAGE "+type_name.toUpperCase());
                                                                                        
                                                                                        while(rs.next())
                                                                                        {
                                                                                            System.out.printf("%20s%n",rs.getString("VAL1"));
                                                                                        }
                                                                                        
                                                                                    }
                                                                                    else if(type_name.equals("blood pressure"))
                                                                                    {
                                                                                        rs = stmt.executeQuery("select round(avg(to_number(SUBSTR(OL.VALUE_1,1,INSTR(OL.VALUE_1,'/')-1))),3) as VAL1 "+
                                                                                                                "from OBSERVATIONS_LOG OL, ASSIGN_DISEASE AD "+
                                                                                                                "WHERE OL.PATIENT_ID = AD.PATIENT_ID "+
                                                                                                                "and OL.OBSERVATION_TYPE_ID = "+type_id+
                                                                                                                " and AD.CLASS_ID = "+class_id+ 
                                                                                                                " and OL.OBSERVATION_DATETIME between to_timestamp('"+obs_start_dt+"','DD/MM/YYYY') and to_timestamp('"+obs_end_dt+"','DD/MM/YYYY')");

                                                                                        System.out.printf("%20s%n","AVERAGE SYSTOLIC "+type_name.toUpperCase());
                                                                                        
                                                                                        while(rs.next())
                                                                                        {
                                                                                            System.out.printf("%20s%n",rs.getString("VAL1"));
                                                                                        }

                                                                                        rs = stmt.executeQuery("select round(avg(to_number(SUBSTR(OL.VALUE_1,INSTR(OL.VALUE_1,'/')+1))),3) as VAL1 "+
                                                                                                                "from OBSERVATIONS_LOG OL, ASSIGN_DISEASE AD "+
                                                                                                                "WHERE OL.PATIENT_ID = AD.PATIENT_ID "+
                                                                                                                "and OL.OBSERVATION_TYPE_ID = "+type_id+
                                                                                                                " and AD.CLASS_ID = "+class_id+ 
                                                                                                                " and OL.OBSERVATION_DATETIME between to_timestamp('"+obs_start_dt+"','DD/MM/YYYY') and to_timestamp('"+obs_end_dt+"','DD/MM/YYYY')");
                                                                                        
                                                                                        System.out.printf("%20s%n","AVERAGE DIASTOLIC "+type_name.toUpperCase());
                                                                                        
                                                                                        while(rs.next())
                                                                                        {
                                                                                            System.out.printf("%20s%n",rs.getString("VAL1"));
                                                                                        }
                                                                                        
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        System.out.println("No aggregated reports available for this observation type!!");
                                                                                    }

                                                                                }
                                                                                else
                                                                                {
                                                                                    break;
                                                                                }                                                                                                
                                                                            }
                                                                        }
                                                                        else
                                                                        {
                                                                            break;
                                                                        }
                                                                    }
				        					
				        									        					
				        				
				        			}
                                                                
				        			else 
				        			{
				        				break;
				        			}
			        			}
			        		}
                                                
                                                else if(phyisican_input==4)
                                                {       
                                                    while(true)
                                                    {
                                                        System.out.print("Enter 1.Assign Patient to a Disease class  0.Back:");
                                                        int ip_doc = Integer.parseInt(br.readLine());
                                                    
                                                        if(ip_doc==1)
                                                        {
                                                        

                                                            rs = stmt.executeQuery("select PATIENT_ID, PATIENT_NAME from PATIENT" );
                                                            System.out.println("List of Patients \n");
                                                            System.out.println("PATIENT_ID : PATIENT_NAME");

                                                            while(rs.next())
                                                            {
                                                                String PATIENT_ID = rs.getString("PATIENT_ID");
                                                                String PATIENT_NAME = rs.getString("PATIENT_NAME");
                                                                System.out.println(PATIENT_ID+" : "+PATIENT_NAME);
                                                            }

                                                            System.out.print("\n Enter Patient ID from the list above :");
                                                            String patient_id = br.readLine();

                                                            rs = stmt.executeQuery("SELECT CLASS_ID,CLASS_NAME FROM DISEASE_CLASS WHERE LOWER(CLASS_NAME) <> 'general'");
                                                            System.out.println("\n \n List of Disease Classes \n");
                                                            System.out.println("CLASS_ID : CLASS_NAME");

                                                            while(rs.next())
                                                            {
                                                                String CLASS_ID = rs.getString("CLASS_ID");
                                                                String CLASS_NAME = rs.getString("CLASS_NAME");
                                                                System.out.println(CLASS_ID+" : "+CLASS_NAME);
                                                            }

                                                            System.out.print("\n Enter Class ID from the list above :");
                                                            int class_id = Integer.parseInt(br.readLine());

                                                            //check if patient is already assigned to the disease class
                                                            rs = stmt.executeQuery("SELECT * FROM ASSIGN_DISEASE WHERE PATIENT_ID = '" + patient_id + "' AND CLASS_ID = " + class_id + " AND CLASS_ID NOT IN (SELECT CLASS_ID FROM DISEASE_CLASS WHERE LOWER(CLASS_NAME)='general')");
                                                                if(!rs.next())
                                                                {
                                                                    int execute = stmt.executeUpdate("insert into ASSIGN_DISEASE VALUES ('" + patient_id + "'," + class_id +",'"+username+"')" );
                                                                    
                                                                    
                                                                    if(execute==1)
                                                                    {
                                                                        stmt.executeUpdate("COMMIT");
                                                                        System.out.println("Patient successfully assigned to disease class.");
                                                                    }        
                                                                    else
                                                                    {
                                                                        stmt.executeUpdate("ROLLBACK");
                                                                        System.out.println("Patient could not be assigned to disease class.");
                                                                    }

                                                                }
                                                                else
                                                                {
                                                                    System.out.println("The patient is already assigned to the disease class.");                                                                
                                                                }
                                                        }
                                                        else
                                                        {
                                                            break;
                                                        }
                                                    }

                                                }
			        		else
			        		{
			        			break;
			        		}
		        		}	 
		        	}
		        	else if(match==2)
		        	{
		        		//inside patient
		        		while(true)
		        		{
		        			System.out.print("Choose 1.Enter Observations 2.View Observations 3.Add a New Observation Type"+" "+
		        			"4.View MyAlerts 5.Manage HealthFriends 6.Back:");
		        			int patient_choice = Integer.parseInt(br.readLine());
		        		
		        			if(patient_choice==1)
		        			{
								while(true)
								{
										        				
			        				//enter observations
			        				System.out.println("Choose Observation Type (enter corresponding number), 0-Back:");
			        				//show all the related observations for a patient with ids of the types
                                                                
                                                                rs = stmt.executeQuery("select OT.TYPE_ID, OT.TYPE_NAME" + 
                                                                                        " from ASSIGN_DISEASE AD, DISEASE_OBSERVATION_TYPE DOT, OBSERVATION_TYPE OT" + 
                                                                                        " where AD.PATIENT_ID = '"+username+"' and AD.CLASS_ID = DOT.CLASS_ID and DOT.TYPE_ID = OT.TYPE_ID" + 
                                                                                        " UNION" +
                                                                                        " select OT.TYPE_ID, OT.TYPE_NAME "+
                                                                                        " from OBSERVATION_TYPE OT, DISEASE_OBSERVATION_TYPE DOT"+
                                                                                        " where OT.TYPE_ID = DOT.TYPE_ID and"+
                                                                                        " DOT.CLASS_ID in (select CLASS_ID from DISEASE_CLASS where lower(CLASS_NAME) = 'general')");

                                                                
                                                                while(rs.next())
                                                                 {
                                                                    String TYPE_ID = rs.getString("TYPE_ID");
                                                                    String TYPE_NAME = rs.getString("TYPE_NAME");
                                                                    System.out.println(TYPE_ID+" : "+TYPE_NAME);
                                                                 }
                                                                
                                                                int obv_choice = Integer.parseInt(br.readLine());
			        				
			        				//date of recording will be sysdate
			        				if(obv_choice==0)
			        				{
			        					break;
			        				}
			        				else
			        				{
	     				
				        				System.out.print("Enter observation date in format - DD/MM/YYYY HH:MM:SS AM/PM :");
				        				String obs_dt = br.readLine();
				        				rs = stmt.executeQuery("select ADDITIONAL_INFO_1 as info_1, "+
                                                                                                "case when ADDITIONAL_INFO_2 is null then '0' else ADDITIONAL_INFO_2 end as info_2 "+
                                                                                                "from OBSERVATION_TYPE where TYPE_ID = "+obv_choice);	
                                                                        String info_1="";
                                                                        String info_2="";                                                                        
                                                                        while(rs.next())
                                                                         {
                                                                            info_1 = rs.getString("info_1");
                                                                            info_2 = rs.getString("info_2");                                                                            
                                                                         }
                                                                
                                                                        System.out.println("Enter the following :");
                                                                        //select attribute choice 1
                                                                        System.out.print(info_1+" :");
                                                                        String attribute1_val = br.readLine();
                                                                        
                                                                        String attribute2_val="";
                                                                        if(info_2.length()>1)
                                                                        {
                                                                            //select attribute choice 2
                                                                            System.out.print(info_2+" :");
                                                                            attribute2_val = br.readLine();
                                                                        }
                                                                          
                                                                        //insert the data.
                                                                        int execute;
                                                                        if(info_2.length()<=1)
                                                                        {
                                                                            execute = stmt.executeUpdate("insert into OBSERVATIONS_LOG(PATIENT_ID,OBSERVATION_TYPE_ID,VALUE_1,OBSERVATION_DATETIME) values('" + username + "'," +obv_choice +",'"+attribute1_val + "','" + obs_dt+"')" );
                                                                        }
                                                                        else
                                                                        {
                                                                            execute = stmt.executeUpdate("insert into OBSERVATIONS_LOG(PATIENT_ID,OBSERVATION_TYPE_ID,VALUE_1,VALUE_2,OBSERVATION_DATETIME) values('" + username + "'," +obv_choice + ",'"+attribute1_val + "','"+attribute2_val + "','" + obs_dt+"')" );
                                                                        }
//                                                                      
                                                                                                                                                                                                                       
                                                                        if(execute==1)
                                                                        {
                                                                            stmt.executeUpdate("COMMIT");
                                                                            System.out.println("New observation inserted");
                                                                        }  	
                                                                        else
                                                                        {
                                                                            stmt.executeUpdate("ROLLBACK");
                                                                            System.out.println("Observation could not be inserted");
                                                                        }
			        				}
								}
		        				
		        			}
		        			else if(patient_choice==2)
		        			{
		        				//view observations
		        				while(true)
		        				{
		        					System.out.println("Choose Observation Type, 0-Back:");
			        				//show all the related observations for a patient with ids of the types
                                                                rs = stmt.executeQuery("select OT.TYPE_ID, OT.TYPE_NAME" + 
                                                                                        " from ASSIGN_DISEASE AD, DISEASE_OBSERVATION_TYPE DOT, OBSERVATION_TYPE OT" + 
                                                                                        " where AD.PATIENT_ID = '"+username+"' and AD.CLASS_ID = DOT.CLASS_ID and DOT.TYPE_ID = OT.TYPE_ID" + 
                                                                                        " UNION" +
                                                                                        " select OT.TYPE_ID, OT.TYPE_NAME "+
                                                                                        " from OBSERVATION_TYPE OT, DISEASE_OBSERVATION_TYPE DOT"+
                                                                                        " where OT.TYPE_ID = DOT.TYPE_ID and"+
                                                                                        " DOT.CLASS_ID in (select CLASS_ID from DISEASE_CLASS where lower(CLASS_NAME) = 'general')");

                                                                
                                                                while(rs.next())
                                                                 {
                                                                    String TYPE_ID = rs.getString("TYPE_ID");
                                                                    String TYPE_NAME = rs.getString("TYPE_NAME");
                                                                    System.out.println(TYPE_ID+" : "+TYPE_NAME);
                                                                 }
                                                                
			        				int obv_choice = Integer.parseInt(br.readLine());
			        				
			        				if(obv_choice==0)
			        				{
			        					break;
			        				}
			        				else
			        				{
			        					//display observations for the particular obv type id.
                                                                    rs = stmt.executeQuery("select OL.OBSERVATION_ID,OT.TYPE_NAME,OL.VALUE_1,"+
                                                                                            " case when OL.VALUE_2 is null then 'NA' else OL.VALUE_2 end as VALUE_2, OL.OBSERVATION_DATETIME,OL.RECORDED_DATETIME"+
                                                                                            " from observations_log OL, observation_type OT where OL.OBSERVATION_TYPE_ID=OT.TYPE_ID"+
                                                                                            " and OT.TYPE_ID="+obv_choice+" and PATIENT_ID='"+username+"'" );
                                                                
                                                                    System.out.printf("%13s  %17s   %7s   %7s   %21s   %21s%n", "OBSERVATION_ID", "OBSERVATION_TYPE", "VALUE_1", "VALUE_2", "OBSERVATION_DATETIME", "RECORDED_DATETIME");

                                                                                               
                                                                                                    
                                                                    while(rs.next())
                                                                     {
                                                                        java.sql.Timestamp OBS_DATETIME = rs.getTimestamp("OBSERVATION_DATETIME");
                                                                        String OBS_DATETIME_STR = OBS_DATETIME.toString();

                                                                        java.sql.Timestamp REC_DATETIME = rs.getTimestamp("RECORDED_DATETIME");
                                                                        String REC_DATETIME_STR = REC_DATETIME.toString();

                                                                        String OBSERVATION_ID = rs.getString("OBSERVATION_ID");
                                                                        String TYPE_NAME = rs.getString("TYPE_NAME");
                                                                        String VALUE_1 = rs.getString("VALUE_1");
                                                                        String VALUE_2 = rs.getString("VALUE_2");    
                                                                        //System.out.println(TYPE_NAME+"\t"+VALUE_1+"\t"+VALUE_2+"\t"+OBS_DATETIME+"\t"+REC_DATETIME);                                                                               
                                                                        System.out.printf("%13s  %17s   %7s   %7s   %23s   %23s%n", OBSERVATION_ID, TYPE_NAME, VALUE_1, VALUE_2, OBS_DATETIME_STR, REC_DATETIME_STR);
                                                                     }
                                                                                                
			        				}
		        				}
		        			}
		        			else if(patient_choice==3)
		        			{
		        				//add observation type
		        				System.out.print("Enter Observation Type name:");
			        			String new_obv_type = br.readLine();
			        			
			        			System.out.println("Enter Observation Category ID:");
                                                        rs = stmt.executeQuery("select CATEGORY_ID,CATEGORY_NAME from OBSERVATION_CATEGORY");
                                                        
                                                        while(rs.next())
                                                        {
                                                             System.out.println(rs.getString("CATEGORY_ID") + ":" + rs.getString("CATEGORY_NAME") );
                                                        }
			        			int obv_cat = Integer.parseInt(br.readLine());
			        			
			        			//enter attribute names
			        			System.out.print("Enter Observation Type attribute 1:");
			        			String new_obv_att1 = br.readLine();
			        			
			        			System.out.print("Enter Observation Type attribute 2, if not enter NA:");
			        			String new_obv_att2 = br.readLine();
                                                        if(new_obv_att2.equals("NA"))
                                                        {
                                                            new_obv_att2 = "";
                                                        }
                                                        
                                                        System.out.print("Enter Threshold value, if applicable,else NA:");
			        			String threshold = br.readLine();
                                                        if(threshold.equals("NA"))
                                                        {
                                                            threshold = "";
                                                        }
			        			
			        			System.out.println("Press 1 to confirm, else 0");
			        			int choice = Integer.parseInt(br.readLine());
			        			
			        			if(choice==1)
			        			{
                                                            rs = stmt.executeQuery("select * from OBSERVATION_TYPE B where lower(B.TYPE_NAME)= '" + new_obv_type.toLowerCase() + "'");
                                                            
                                                            if(rs.next())
                                                            {
					
                                                                System.out.println("The observation type " + new_obv_type + " already exists!!");
	        		
                                                            }
                                                            else
                                                            {
                                                                //insert row in observation type
                                                                //System.out.println("insert into OBSERVATION_TYPE(TYPE_NAME,THRESHOLD_VAL,ADDITIONAL_INFO_1,ADDITIONAL_INFO_2) VALUES ('"   + new_obv_type + "','" + threshold + "','" + new_obv_att1 + "','" + new_obv_att2 + "')" );
                                                                 int execute1 = stmt.executeUpdate("insert into OBSERVATION_TYPE(TYPE_NAME,THRESHOLD_VAL,ADDITIONAL_INFO_1,ADDITIONAL_INFO_2) VALUES ('"   + new_obv_type + "','" + threshold + "','" + new_obv_att1 + "','" + new_obv_att2 + "')" );
                                                                                                                                  
                                                                 //use the new obv type id and cat id to insert a tuple in table OBSERVATION_TYPE_CATEGORY
                                                                 rs = stmt.executeQuery("select TYPE_ID from OBSERVATION_TYPE where TYPE_NAME = '" + new_obv_type +"'");
                                                                 int obv_type_id = 0;
                                                                 while(rs.next())
                                                                 {
                                                                    obv_type_id = rs.getInt("TYPE_ID");
                                                                 }
                                                                 //System.out.println("insert into OBSERVATION_TYPE_CATEGORY VALUES (" + obv_cat + "," + obv_type_id + ")");
                                                                 int execute2 = stmt.executeUpdate("insert into OBSERVATION_TYPE_CATEGORY VALUES (" + obv_cat + "," + obv_type_id + ")");
                                                                 
                                                                 // use new type id id to insert a tuple in DISEASE_OBSERVATION_TYPE with disease_id of 'genaral'
                                                                 rs = stmt.executeQuery("select CLASS_ID from DISEASE_CLASS where lower(CLASS_NAME) = 'general'");
                                                                 int class_id = 0;
                                                                 while(rs.next())
                                                                 {
                                                                    class_id = rs.getInt("CLASS_ID");
                                                                 }
                                                                 int execute3 = stmt.executeUpdate("insert into DISEASE_OBSERVATION_TYPE VALUES (" + class_id + "," + obv_type_id +")" );

                                                                 if(execute1==1 && execute2==1 && execute3==1)
                                                                 {
                                                                     stmt.executeUpdate("COMMIT");
                                                                     System.out.println("New Observation Type inserted");
                                                                 }      
                                                                 else
                                                                 {
                                                                    stmt.executeUpdate("ROLLBACK");
                                                                    System.out.println("New Observation Type could not be inserted");
                                                                 }
                                                                
                                                            }
                                                            
			        				
			        			}
			        			else
			        			{
			        				//do nothing
			        			}
			        	    	
		        			}
		        			else if(patient_choice==4)
		        			{
		        				//view my alerts
		        				System.out.println("Unread Alerts:");
                                                        //System.out.println("select TYPE_NAME,ADDITIONAL_INFO_1,VALUE_1,ADDITIONAL_INFO_2,VALUE_2,OBSERVATION_DATETIME,RECORDED_DATETIME from OBSERVATIONS_LOG O,MY_ALERTS M, OBSERVATION_TYPE T WHERE O.OBSERVATION_TYPE_ID = T.TYPE_ID AND M.OBSERVATION_ID = O.OBSERVATION_ID AND ALERT_STATUS = '1' AND O.PATIENT_ID ='" + username +"'");
                                                        //display all the alerts from the table - MY_ALERTS where ALERT_STATUS -1 
                                                        rs = stmt.executeQuery("select TYPE_NAME,ADDITIONAL_INFO_1,VALUE_1,ADDITIONAL_INFO_2,case when VALUE_2 is null then 'NA' else VALUE_2 end as VALUE_2,OBSERVATION_DATETIME,RECORDED_DATETIME from OBSERVATIONS_LOG O,MY_ALERTS M, OBSERVATION_TYPE T WHERE O.OBSERVATION_TYPE_ID = T.TYPE_ID AND M.OBSERVATION_ID = O.OBSERVATION_ID AND ALERT_STATUS = '1' AND O.PATIENT_ID ='" + username +"'");
                                                        
                                                        int counter = 0;
                                                        //System.out.println("Observation Type" + "\t" + "\t" + "Value for Additional Info1" + "\t\t" + "Value for Additional Info2" + "\t" + "Date of observation" + "\t" + "Date of Recording");
                                                        System.out.printf("%17s   %7s   %7s   %21s   %21s%n", "OBSERVATION_TYPE", "VALUE_1", "VALUE_2", "OBSERVATION_DATETIME", "RECORDED_DATETIME");
                                                        while(rs.next())
                                                        {
                                                            //System.out.println("inside");
                                                            String type_name = rs.getString("TYPE_NAME");
                                                            //System.out.println(type_name);
                                                            String info1 = rs.getString("ADDITIONAL_INFO_1");
                                                            String val1 = rs.getString("VALUE_1");
                                                            String info2 = rs.getString("ADDITIONAL_INFO_2");
                                                            String val2 = rs.getString("VALUE_2");
                                                            String obv_time = rs.getTimestamp("OBSERVATION_DATETIME").toString();
                                                            String rec_time = rs.getTimestamp("RECORDED_DATETIME").toString();
                                                           
                                                            //System.out.println(type_name + "\t\t" + val1 + "\t\t\t" + val2 + "\t" + obv_time + "\t" + rec_time);
                                                            System.out.printf("%17s   %7s   %7s   %21s   %21s%n", type_name, val1, val2, obv_time, rec_time);
                                                            counter++;
                                                        }
                                                        if(counter==0)
                                                        {
                                                            System.out.println("No Unread Alerts");
                                                        }
                                                        else
                                                        {
                                                        
                                                            //execute the stored procedure to mark all alerts as read - ALERT_STATUS -0

                                                            //System.out.println("executing stored proc");
                                                            String proc_string = "{call UPDATE_ALERTS(?,?)}";
                                                            CallableStatement call = conn.prepareCall(proc_string);
                                                            call.setString(1, username);
                                                            call.registerOutParameter(2, java.sql.Types.NUMERIC);
                                                            call.executeUpdate();
                                                            int row_updated = call.getInt(2);
                                                            //System.out.println("row affected" + row_updated);

                                                            if(counter==row_updated)
                                                            {
                                                                stmt.executeUpdate("COMMIT");
                                                            }
                                                            else
                                                            {
                                                                stmt.executeUpdate("ROLLBACK");
                                                            }
                                                        }
		        			}
		        			else if(patient_choice==5)
		        			{
		        				//manage health friend
		        				while(true)
		        				{
		        				
			        				System.out.print("Choose 1.View existing HealthFriends 2.Find a New HealthFriend" + " "+
										"3.Find a HealthFriend at Risk 4.Back:");
									int friend_choice = Integer.parseInt(br.readLine());
									
									if(friend_choice==1)
									{
										//View existing HealthFriends
										System.out.println("Select a friend id:");
										//select all the friends
                                                                                rs = stmt.executeQuery("select HEALTH_FRIEND_ID from HEALTH_FRIEND where PATIENT_ID = '" + username + "'");
                                                                                int frnd_count =0;
										while(rs.next())
                                                                                {
                                                                                    System.out.println(rs.getString("HEALTH_FRIEND_ID"));
                                                                                    frnd_count++;
                                                                                }
                                                                                
                                                                                if(frnd_count==0)
                                                                                {
                                                                                    System.out.println("No Health Friends!!");
                                                                                }
                                                                                else {
                                                                            
                                                                                    String friend_id = br.readLine();

                                                                                    while(true)
                                                                                    {

                                                                                            System.out.println("Choose 1.friends active alerts 2.observations of the friend 3.Back:");
                                                                                            int friend_menu= Integer.parseInt(br.readLine());

                                                                                            if(friend_menu==1)
                                                                                            {
                                                                                                //friends active alerts
                                                                                                rs = stmt.executeQuery("select TYPE_NAME,ADDITIONAL_INFO_1,VALUE_1,ADDITIONAL_INFO_2,case when VALUE_2 is null then 'NA' else VALUE_2 end as VALUE_2,OBSERVATION_DATETIME,RECORDED_DATETIME from OBSERVATIONS_LOG O,MY_ALERTS M, OBSERVATION_TYPE T WHERE O.OBSERVATION_TYPE_ID = T.TYPE_ID AND M.OBSERVATION_ID = O.OBSERVATION_ID AND ALERT_STATUS = '1' AND O.PATIENT_ID ='" + friend_id +"'");
                                                                                                int counter = 0;
                                                                                                //System.out.println("Observation Type" + "\t" + "Additional Info1" + "\t" + "Value for Additional Info1" + "\t" + "Additional Info2" + "\t" + "Value for Additional Info1" + "\t" + "Date of observation" + "\t" + "Date of Recording");
                                                                                                System.out.printf("%17s   %7s   %7s   %21s   %21s%n", "OBSERVATION_TYPE", "VALUE_1", "VALUE_2", "OBSERVATION_DATETIME", "RECORDED_DATETIME");
                                                                                                while(rs.next())
                                                                                                {
                                                                                                         String type_name = rs.getString("TYPE_NAME");
                                                                                                        //System.out.println(type_name);
                                                                                                        String info1 = rs.getString("ADDITIONAL_INFO_1");
                                                                                                        String val1 = rs.getString("VALUE_1");
                                                                                                        String info2 = rs.getString("ADDITIONAL_INFO_2");
                                                                                                        String val2 = rs.getString("VALUE_2");
                                                                                                        String obv_time = rs.getTimestamp("OBSERVATION_DATETIME").toString();
                                                                                                        String rec_time = rs.getTimestamp("RECORDED_DATETIME").toString();

                                                                                                    //System.out.println(type_name + "\t\t" + info1 + "\t\t\t\t" + val1 + "\t" + info2 + "\t\t\t" + val2 + "\t\t\t\t" + obv_time + "\t" + rec_time);
                                                                                                    System.out.printf("%17s   %7s   %7s   %21s   %21s%n", type_name, val1, val2, obv_time, rec_time);
                                                                                                    counter++;
                                                                                                }
                                                                                                if(counter==0)
                                                                                                {
                                                                                                    System.out.println("No Unread Alerts for this friend!!");
                                                                                                }
                                                                                            }
                                                                                            else if(friend_menu==2)
                                                                                            {
                                                                                                //observations of the friend
                                                                                                rs = stmt.executeQuery("select OT.TYPE_ID, OT.TYPE_NAME" + 
                                                                                                                        " from ASSIGN_DISEASE AD, DISEASE_OBSERVATION_TYPE DOT, OBSERVATION_TYPE OT" + 
                                                                                                                        " where AD.PATIENT_ID = '"+friend_id+"' and AD.CLASS_ID = DOT.CLASS_ID and DOT.TYPE_ID = OT.TYPE_ID" + 
                                                                                                                        " UNION" +
                                                                                                                        " select OT.TYPE_ID, OT.TYPE_NAME "+
                                                                                                                        " from OBSERVATION_TYPE OT, DISEASE_OBSERVATION_TYPE DOT"+
                                                                                                                        " where OT.TYPE_ID = DOT.TYPE_ID and"+
                                                                                                                        " DOT.CLASS_ID in (select CLASS_ID from DISEASE_CLASS where lower(CLASS_NAME) = 'general')");

                                                                                                while(rs.next())
                                                                                                {
                                                                                                   String TYPE_ID = rs.getString("TYPE_ID");
                                                                                                   String TYPE_NAME = rs.getString("TYPE_NAME");
                                                                                                   System.out.println(TYPE_ID+" : "+TYPE_NAME);
                                                                                                }

                                                                                                int obv_choice = Integer.parseInt(br.readLine());
                                                                                                if(obv_choice==0)
                                                                                                {
                                                                                                        break;
                                                                                                }

                                                                                                else
                                                                                                {
                                                                                                        //display observations for the particular obv type id.
                                                                                                    rs = stmt.executeQuery("select OL.OBSERVATION_ID,OT.TYPE_NAME,OL.VALUE_1,"+
                                                                                                                            " case when OL.VALUE_2 is null then 'NA' else OL.VALUE_2 end as VALUE_2,OL.OBSERVATION_DATETIME,OL.RECORDED_DATETIME"+
                                                                                                                            " from observations_log OL, observation_type OT where OL.OBSERVATION_TYPE_ID=OT.TYPE_ID"+
                                                                                                                            " and OT.TYPE_ID="+obv_choice+" and PATIENT_ID='"+friend_id+"'" );

                                                                                                    System.out.printf("%13s  %17s   %7s   %7s   %21s   %21s%n", "OBSERVATION_ID", "OBSERVATION_TYPE", "VALUE_1", "VALUE_2", "OBSERVATION_DATETIME", "RECORDED_DATETIME");



                                                                                                    while(rs.next())
                                                                                                     {
                                                                                                        java.sql.Timestamp OBS_DATETIME = rs.getTimestamp("OBSERVATION_DATETIME");
                                                                                                        String OBS_DATETIME_STR = OBS_DATETIME.toString();

                                                                                                        java.sql.Timestamp REC_DATETIME = rs.getTimestamp("RECORDED_DATETIME");
                                                                                                        String REC_DATETIME_STR = REC_DATETIME.toString();

                                                                                                        String OBSERVATION_ID = rs.getString("OBSERVATION_ID");
                                                                                                        String TYPE_NAME = rs.getString("TYPE_NAME");
                                                                                                        String VALUE_1 = rs.getString("VALUE_1");
                                                                                                        String VALUE_2 = rs.getString("VALUE_2");    
                                                                                                        //System.out.println(TYPE_NAME+"\t"+VALUE_1+"\t"+VALUE_2+"\t"+OBS_DATETIME+"\t"+REC_DATETIME);                                                                               
                                                                                                        System.out.printf("%13s  %17s   %7s   %7s   %23s   %23s%n", OBSERVATION_ID, TYPE_NAME, VALUE_1, VALUE_2, OBS_DATETIME_STR, REC_DATETIME_STR);
                                                                                                     }

                                                                                                }



                                                                                            }
                                                                                            else
                                                                                            {
                                                                                                    break;
                                                                                            }
                                                                                    }
                                                                            }

                                                                            }
                                                                            else if(friend_choice==2)
                                                                            {
                                                                                    //find a new friend
                                                                                while(true)
                                                                                {
                                                                                    System.out.println("Choose an id that you would like to be friend, 0-Back:");
                                                                                    //display all the ids of patients which are not his friend already
                                                                                    //and suffering from the same disease as he his - can be more than 1 disease
                                                                                    rs = stmt.executeQuery("SELECT PATIENT_ID,PATIENT_NAME FROM PATIENT WHERE PATIENT_ID IN (SELECT PATIENT_ID FROM ASSIGN_DISEASE WHERE CLASS_ID IN (SELECT a.CLASS_ID FROM ASSIGN_DISEASE a,DISEASE_CLASS c WHERE a.CLASS_ID = c.CLASS_ID and PATIENT_ID = '" 
                                                                                                        +username + "' and LOWER(c.CLASS_NAME) <> 'general') AND PATIENT_ID <> '" + username 
                                                                                                        + "'MINUS SELECT HEALTH_FRIEND_ID FROM HEALTH_FRIEND WHERE PATIENT_ID = '" + username +"') AND PUBLIC_STATUS = 'Y'");
                                                                                    int cntr = 0;
                                                                                    while(rs.next())
                                                                                    {
                                                                                        String friend_id = rs.getString("PATIENT_ID");
                                                                                        String friend_name = rs.getString("PATIENT_NAME");
                                                                                        System.out.println(friend_id + ": " + friend_name);
                                                                                        cntr++;
                                                                                    }
                                                                                    
                                                                                    if(cntr==0)
                                                                                    {
                                                                                        System.out.println("No Friends available!!");
                                                                                        break;
                                                                                    }

                                                                                    String friend_id= br.readLine();

                                                                                    if(friend_id.equals("0"))
                                                                                    {
                                                                                        break;
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        System.out.println("Press 1 to confirm, else 0");
                                                                                        int choice = Integer.parseInt(br.readLine());

                                                                                        if(choice==1)
                                                                                        {
                                                                                            //insert row in HEALTH_FRIEND
                                                                                               int cnt = stmt.executeUpdate("INSERT INTO HEALTH_FRIEND VALUES('" + username + "','" + friend_id + "',SYSDATE)" );
                                                                                               int cnt2 = stmt.executeUpdate("INSERT INTO HEALTH_FRIEND VALUES('" + friend_id + "','" + username + "',SYSDATE)" );
                                                                                               if(cnt==1 && cnt2==1)
                                                                                               {
                                                                                                   stmt.executeUpdate("COMMIT");
                                                                                                   System.out.println("New Friend Added");
                                                                                               }
                                                                                               else 
                                                                                               {
                                                                                                   stmt.executeUpdate("ROLLBACK");
                                                                                               }


                                                                                        }
                                                                                        else
                                                                                        {
                                                                                                //do nothing
                                                                                        }
                                                                                    }

                                                                            }
                                                                        }
									else if(friend_choice==3)
									{
										//find friend at risk
										while(true)
										{
										
											System.out.println("Choose an friend id whose is at risk, 0-Back:");
											//display all friends with more than 5 alerts
											rs = stmt.executeQuery("select hf.HEALTH_FRIEND_ID from HEALTH_FRIEND hf where hf.HEALTH_FRIEND_ID IN (\n" +
                                                                                                                "select l.patient_id from observations_log l, my_alerts a where l.OBSERVATION_ID = a.OBSERVATION_ID and alert_status = '1' and systimestamp >= (OBSERVATION_DATETIME + 7)\n" +
                                                                                                                "group by l.patient_id having count(*) >= 5\n" +
                                                                                                                ") and hf.patient_id ='" +username + "'");
                                                                                        int counter = 0;
                                                                                        
                                                                                        while(rs.next())
                                                                                        {
                                                                                            System.out.println(rs.getString("HEALTH_FRIEND_ID"));
                                                                                            counter++;
                                                                                        }
                                                                                        
                                                                                        //exit if no friends at risk
                                                                                        if(counter==0)
                                                                                        {
                                                                                            System.out.println("You have no health friends at risk!!");
                                                                                            break;
                                                                                        }
                                                                                        else
                                                                                        {
                                                                         
                                                                                            String friend_id= br.readLine();
                                                                                            if(friend_id.equals("0"))
                                                                                            {
                                                                                                    break;
                                                                                            }
                                                                                            else
                                                                                            {

                                                                                                    System.out.print("Enter the message you want to send:");
                                                                                                    //enter the message

                                                                                                    String message= br.readLine();

                                                                                                    System.out.println("Press 1 to confirm, else 0");
                                                                                                    int choice = Integer.parseInt(br.readLine());

                                                                                                    if(choice==1)
                                                                                                    {
                                                                                                            int row_insert = stmt.executeUpdate("INSERT INTO HEALTH_FRIEND_ALERTS(HEALTH_FRIEND_ID,PATIENT_ID,MESSAGE,MSG_DATE) VALUES ('"+ username + "','" +friend_id + "','" + message + "',SYSDATE)");
                                                                                                            if(row_insert == 1)
                                                                                                            {
                                                                                                                stmt.executeUpdate("COMMIT");
                                                                                                                System.out.println("Message sent!!");
                                                                                                            }
                                                                                                            else
                                                                                                            {
                                                                                                                stmt.executeUpdate("ROLLBACK");
                                                                                                                System.out.println("Message cannot be sent!!");
                                                                                                            }
                                                                                                            //insert row in HEALTH_FRIEND_ALERTS
                                                                                                    }
                                                                                                    else
                                                                                                    {
                                                                                                            //do nothing
                                                                                                    }
                                                                                            }
                                                                                        }
										}									
										
									}
									else if(friend_choice==4)
									{
										break;
									}
		        				}
									
								
		        			}
		        			else if(patient_choice==6)
		        			{
		        				break;
		        			}
		        			else
		        			{
		        				System.out.println("Wrong Option selected");
		        			}
		        		}
		        	        		
		        	}
		        	else 
		        	{
		        		System.out.println("Username and password do not match");
		        	}
		        	
		        }
		        else if(user_input==2)
		        {
					//create new user
		        	System.out.print("Enter 1.Physician 2.Patient:");
		    	   	int user_type = Integer.parseInt(br.readLine());
		        
		        	if(user_type==1)
			        {
			        	System.out.print("Enter Physician Name:");
			        	String physicianName = br.readLine();
			        	System.out.print("Enter Username (case sensitive) :");
			        	String username = br.readLine();
			        	
			        	System.out.print("Enter Password (case sensitive) :");
			        	//Console console = System.console();
			           	//char[] passwordChars = console.readPassword();
			        	//String password = new String(passwordChars);	
			        	String password = br.readLine();
                                        
			        	System.out.print("Enter Clinic Name:");
			        	String clinic = br.readLine();
			        			        	
			        	System.out.println("Press 1 to confirm, else 0:");
						int choice = Integer.parseInt(br.readLine());
							        			
						if(choice==1)
						{
							rs = stmt.executeQuery("select * from USER_LOGIN where USER_ID = '" + username + "'");
							if(!rs.next())
							{
						
								//insert details into user_login and physician tables
								int count1=stmt.executeUpdate("INSERT INTO USER_LOGIN VALUES(" + "'" + username + "','" + password + "','0')");
								
								//System.out.println("INSERT INTO USER_LOGIN VALUES(" + "'" + username + "','" + password + "','0')");
								int count2=stmt.executeUpdate("INSERT INTO PHYSICIAN VALUES(" + "'" + username + "','" + physicianName + "','" + clinic + "')");
								
								//System.out.println"INSERT INTO PHYSICIAN VALUES(" + "'" + username + "','" + physicianName + "','" + clinic + "')";
								if(count1==1 && count2==1)
								{
                                                                    stmt.executeUpdate("COMMIT");
                                                                    System.out.println("User created");
								}
								else
								{
                                                                    stmt.executeUpdate("ROLLBACK");
                                                                    System.out.println("User could not be Created");
								}
							
							}
							else
							{
								System.out.println("Username already exists!!Try again");
							}	
						}
						else
						{
							//do nothing
						}
			        }
		 
		        	else if(user_type==2)
		        	{
		        	  		//take patient details as input and insert in DB
		        	  	System.out.print("Enter Patient Name:");
			        	String patientName = br.readLine();
			        	
			        	System.out.print("Enter Username (case sensitive) :");
			        	String username = br.readLine();
			        				        	     	
			        	System.out.print("Enter Password (case sensitive) :");
			        	//Console console = System.console();
			           	//char[] passwordChars = console.readPassword();
			        	//String password = new String(passwordChars);
                                        String password = br.readLine();
			        			        	
			        	System.out.print("Enter Age (eg. 34):");
			        	String age = br.readLine();	
			        		
					String sex="X";
			        	while(true)
			        	{
				        	System.out.print("Enter 1 for Male and 2 for Female:");
			    	   		int gender = Integer.parseInt(br.readLine());
						
			    	   		if(gender==1)
			    	   		{
			    	   			sex = "Male";
			    	   			break;
			    	   		}
				       		else if(gender==2)
				       		{
				        		sex = "Female";
				        		break;
				       		}
				       		else
			        		{
			        		System.out.println("Wrong Option selected");
						}	
			        	}
			        	
			        		
						System.out.print("Enter Street Address:");
			        	String street = br.readLine();
			        	
			        	System.out.print("Enter City:");
			        	String city = br.readLine();
			        	
			        	System.out.print("Enter State (eg. NC for North Carolina):");
			        	String input_state = br.readLine();
			        	String stateName = input_state.toUpperCase();
			        			        	
			        	System.out.print("Enter Zipcode:");
			        	Integer zip = Integer.parseInt(br.readLine());
			        	
			        	System.out.print("Do you want to be listed as a Healthfriend for other patients who have same illness? (Y/N)");
			        	String input = br.readLine();
			        	String publicStatus = input.toUpperCase();
                        
					rs = stmt.executeQuery("select class_id from disease_class where lower(CLASS_NAME) = 'general'");
                                        
                        int class_id=0;
                        while(rs.next())
                        {
                             class_id = rs.getInt("CLASS_ID");
                        }
			        	
			        	System.out.println("Press 1 to confirm, else 0:");
						int choice = Integer.parseInt(br.readLine());
							        			
						if(choice==1)
						{
							rs = stmt.executeQuery("select * from USER_LOGIN where USER_ID = '" + username + "'");
							if(!rs.next())
							{
								//insert details into user_login and PATIENT tables
								int user_login_row = stmt.executeUpdate("INSERT INTO USER_LOGIN VALUES(" + "'" + username + "','" + password + "','1')");
								
								int patient_row = stmt.executeUpdate("INSERT INTO PATIENT VALUES(" + "'" + username + "','" + patientName + "'," + age + ",'" + sex + "','" + publicStatus + "')");
								
								int address_row = stmt.executeUpdate("INSERT INTO PATIENT_ADDRESS(PATIENT_ID,STREET_ADDRESS,CITY,STATE_NAME,ZIP) VALUES(" + "'" + username + "','" + street + "','" + city + "','" + stateName + "'," + zip + ")");
								
                                                                 //ASSIGN GENERAL DISEASE CLASS TO NEW PATIENT
                                                                int assign_disease_row = stmt.executeUpdate("INSERT INTO assign_disease(PATIENT_ID,CLASS_ID) VALUES(" + "'" + username + "'," + class_id + ")");
							
								//	System.out.println("INSERT INTO PATIENT_ADDRESS(PATIENT_ID,STREET_ADDRESS,CITY,STATE_NAME,ZIP) VALUES(" + "'" + username + "','" + street + "','" + city + "','" + stateName + "'," + zip + ")");
					
							
			
								if(user_login_row==1 && patient_row==1 && address_row==1 && assign_disease_row==1)
								{
                                                                    stmt.executeUpdate("COMMIT");
                                                                    System.out.println("User created"); 
								}
								else
								{
                                                                    stmt.executeUpdate("ROLLBACK");
                                                                    System.out.println("User Not Created");
								}
							
							}
							else
							{
								System.out.println("Username already exists!!Try again");
							}					
												
						
						}
						else
						{
							//do nothing
						}
			        
			        		
		        	}
		        	
		        	else
		        	{
		        		System.out.println("Wrong Option selected");
					}
	
		        	
				 }
		        else 
		        {
		        	System.exit(0);
		        }
	    	}
    	  	}
                catch(Throwable oops){
                    stmt.executeUpdate("ROLLBACK");
                    System.out.println("Wrong Option Entered, Program will now exit");
                    oops.printStackTrace();
                }
                finally {
                close(rs);
                close(stmt);
                close(conn);
            }
    }
    catch(Throwable oops) {
    		System.out.println("Wrong Option Entered, Program will now exit");
    		//System.exit(0);
            oops.printStackTrace();
        }
    }
    
    //close stmsts
    static void close(Connection conn) {
        if(conn != null) {
            try { conn.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(Statement st) {
        if(st != null) {
            try { st.close(); } catch(Throwable whatever) {}
        }
    }
    static void close(ResultSet rs) {
        if(rs != null) {
            try { rs.close(); } catch(Throwable whatever) {}
        }
    }
}
