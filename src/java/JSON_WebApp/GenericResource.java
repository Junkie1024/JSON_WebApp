package JSON_WebApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * REST Web Service
 *
 * @author 1894082
 */
@Path("jsoncall")
public class GenericResource {

    
   
      
    @Context
    private UriInfo context;
    static Connection conn = null;
    static Statement stm = null;
    static ResultSet rs = null;
    
    static String jobID, jobTitle, minSalary, maxSalary;

    static JSONObject connError = new JSONObject();

    public GenericResource() {
        
    }

    
    @GET
    @Path("list")
    @Produces("text/plain")
    public String getText() {

        JSONArray arrList = new JSONArray();
        JSONObject objList = new JSONObject();

        conn = getConnection();

        Date date = new Date();
        long time = date.getTime();
        if (conn != null) {
            try {
                String sql = "Select JOB_ID,JOB_TITLE,MIN_SALARY,MAX_SALARY FROM JOBS";
                stm = conn.createStatement();
                int i = stm.executeUpdate(sql);

                rs = stm.executeQuery(sql);

                while (rs.next()) {

                    jobID = rs.getString("JOB_ID");
                    jobTitle = rs.getString("JOB_TITLE");
                    minSalary = rs.getString("MIN_SALARY");
                    maxSalary = rs.getString("MAX_SALARY");
                    
                    
                    objList.accumulate("JobId: ", jobID);
                    objList.accumulate("Job Title", jobTitle);
                    objList.accumulate("Minimum Salary", minSalary);
                    objList.accumulate("Maximum Salary", maxSalary);
                    arrList.add(objList);
                    objList.clear();

                }
//                return mainarrList.toString();
            } catch (SQLException ex) {
                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeConnection();
            }
        } else {

            objList.accumulate("status", " Error");
            objList.accumulate("Timestamp", date);
            objList.accumulate("Message", "Error in display list");
            ;
        }
        return arrList.toString();

    }

    public static Connection getConnection() {

        try {

            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conn;
    }
    
     public static void closeConnection() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                /* ignored */
            }
        }
        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                /* ignored */
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                /* ignored */
            }
        }
    }


    @GET
    @Path("insert&{jobid}&{jobtitle}&{maxSalary}&{minSalary}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getText7(@PathParam("jobid") int jobID,@PathParam("jobtitle") String jobTitle,@PathParam("maxSalary") int maxSalary,@PathParam("minSalary")int minSalary){

        conn = getConnection();
        JSONObject singleInsert = new JSONObject();

        String sql = "INSERT INTO JOBS("+jobID+","+jobTitle+","+maxSalary+","+minSalary+")";

        if (conn != null) {
            try {
                stm = conn.createStatement();
                int i = stm.executeUpdate(sql);

                if (i > 0) {
                    singleInsert.accumulate("message", "Record inserted");
                    System.out.println(singleInsert);

                } else {
                    singleInsert.accumulate("message", "Record Not inserted");
                    System.out.println(singleInsert);
                }

            } catch (SQLException ex) {
                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeConnection();
            }
        } else {

            connError.accumulate("message", "Connection Error");
            System.out.println(connError);

        }
        
        return null;
    }
/*
    @GET
    @Path("update&{jobid}&{jobtitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getText7(@PathParam("jobid") int jobID,@PathParam("jobtitle") String jobTitle){
        conn = getConnection();
        JSONObject singleupdate = new JSONObject();
        String sql = "UPDATE JOBS SET JOB_TITLE='Student Admin' WHERE JOB_ID = 'YM' ";

        if (conn != null) {
            try {
                stm = conn.createStatement();

                int i = stm.executeUpdate(sql);

                if (i > 0) {
                    singleupdate.accumulate("message", "Record Updated");
                    System.out.println(singleupdate);
                } else {
                    singleupdate.accumulate("message", "Record Not Updated");
                    System.out.println(singleupdate);
                }

            } catch (SQLException ex) {
                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeConnection();
            }
        } else {
            connError.accumulate("message", "Connection Error");
            System.out.println(connError);
        }
    }*/
    

    public static void deleteData() {

        conn = getConnection();
        JSONObject singleDelete = new JSONObject();

        String sql = "DELETE FROM JOBS WHERE JOB_ID = 'YM'";

        if (conn != null) {
            try {
                stm = conn.createStatement();
                int i = stm.executeUpdate(sql);
                if (i > 0) {
                    singleDelete.accumulate("message", "Record Deleted");
                    System.out.println(singleDelete);
                } else {
                    singleDelete.accumulate("message", "Record not Deleted");
                    System.out.println(singleDelete);
                }

            } catch (SQLException ex) {
                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeConnection();
            }
        } else {
            connError.accumulate("message", "Connection Error");
            System.out.println(connError);
        }
    }

    /*public static void getSingleList() {

        conn = getConnection();
        JSONObject singleList = new JSONObject();

        String sql = "SELECT * FROM JOBS WHERE JOB_ID = 'YM'";

        if (conn != null) {
            try {
                stm = conn.createStatement();
                int i = stm.executeUpdate(sql);

                rs = stm.executeQuery(sql);

                while (rs.next()) {

                    countryId = rs.getString("country_id");
                    countryName = rs.getString("country_name");
                    regionId = rs.getString("region_id");
                    singleList.accumulate("countryId", countryId);
                    singleList.accumulate("countryName", countryName);
                    singleList.accumulate("regionId", regionId);
                }
                System.out.println(singleList);

            } catch (SQLException ex) {
                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeConnection();
            }
        } else {
            connError.accumulate("message", "Connection Error");
            System.out.println(connError);
        }
    }*/

   

    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}
