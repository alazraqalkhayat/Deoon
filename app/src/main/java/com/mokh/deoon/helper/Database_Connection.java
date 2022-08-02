package com.mokh.deoon.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mokh.deoon.models.Debentures_model;
import com.mokh.deoon.models.Depits_model;
import com.mokh.deoon.models.All_customers_model;
import com.mokh.deoon.models.All_workrers_model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Database_Connection extends SQLiteOpenHelper {


    static final String DBNAME="azraq.db";

    public Database_Connection(Context context){
        super(context,DBNAME,null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table  If Not EXISTS debits(depits_id INTEGER PRIMARY KEY AUTOINCREMENT,customer_name TEXT,employee_name TEXT,description TEXT,deserved_amount INTEGER,hand TEXT,date TEXT,FOREIGN KEY ('customer_name')REFERENCES customers('customer_name'),FOREIGN KEY ('employee_name')REFERENCES employees('employee_name'))");
        db.execSQL("create table  If Not EXISTS debentures(debenture_id INTEGER PRIMARY KEY AUTOINCREMENT ,cus_name TEXT,emp_name TEXT,money_paied INTEGER,date Text,FOREIGN KEY ('cus_name')REFERENCES customers('customer_name'),FOREIGN KEY ('emp_name')REFERENCES employees('employee_name'))");
        db.execSQL("create table  If Not EXISTS customers(customer_name TEXT PRIMARY KEY ,customer_phone_number TEXT)");
        db.execSQL("create table  If Not EXISTS employees(employee_name TEXT PRIMARY KEY )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table If  EXISTS debits");
        db.execSQL("Drop table If  EXISTS debentures");
        db.execSQL("Drop table If  EXISTS customers");
        db.execSQL("Drop table If  EXISTS employees");


        onCreate(db);
    }

    //start employees tables

    public boolean insertEmployee(String employeeName){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("employee_name",employeeName);


        long result= db.insert("employees",null,contentValues);



        if(result==-1)
            return false;
        else
            return true;


    }

    public Map<Integer,String> getAllEmployees(){

        Map<Integer,String> result =new LinkedHashMap<Integer,String>();
        Integer i=0;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select employee_name from  employees ",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){

            String employee_name =res.getString(res.getColumnIndex("employee_name"));

            result.put(i,employee_name);
            i++;
            res.moveToNext();
        }

        return result;

    }

    public String getEmployeeName(String employeeName){

        String customerDetails="";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select employee_name from  employees where employee_name='"+employeeName+"'",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){

            customerDetails=res.getString(res.getColumnIndex("employee_name"));

            res.moveToNext();
        }

        return customerDetails;
    }

    public boolean updateWorkerOnWorkersTable(String workerNameBefore,String workerNameAfter){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put("employee_name",workerNameAfter);
        long result=db.update("employees",contentValues,"employee_name='"+workerNameBefore+"'",null);

        if(result==-1)
            return false;
        else
            return true;

    }

    public boolean updateWorkerNameOnDebitsTable(String workerNameBefore,String workerNameAfter){

        SQLiteDatabase db=this.getWritableDatabase();


        ContentValues contentValues=new ContentValues();
        contentValues.put("employee_name",workerNameAfter);

        long result=db.update("debits",contentValues,"employee_name='"+workerNameBefore+"'",null);

        if(result==-1)
            return false;
        else
            return true;

    }

    public boolean updateWorkerNameOnDebenturesTable(String workerNameBefore,String workerNameAfter){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("emp_name",workerNameAfter);

        long result=db.update("debentures",contentValues,"emp_name='"+workerNameBefore+"'",null);

        if(result==-1)
            return false;
        else
            return true;

    }


    //end employees tables

    //start customers tables

    public boolean insertCustomerName(String customerName){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("customer_name",customerName);

        long result= db.insert("customers",null,contentValues);

        if(result==-1)
            return false;
        else
            return true;


    }

    public boolean insertCustomer(String customerName,String customerPhoneNumber){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("customer_name",customerName);
        contentValues.put("customer_phone_number",customerPhoneNumber);


        long result= db.insert("customers",null,contentValues);


        if(result==-1)
            return false;
        else
            return true;


    }

    public boolean updateCustomerOnCustomerTable(String customerNameBefore,String customerNameAfter, String customerPhoneNumber){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put("customer_name",customerNameAfter);
        contentValues.put("customer_phone_number",customerPhoneNumber);

        long result=db.update("customers",contentValues,"customer_name='"+customerNameBefore+"'",null);

        if(result==-1)
            return false;
        else
            return true;

    }

    public boolean updateCustomerNameOnDebitsTable(String customerNameBefore,String customerNameAfter){

        SQLiteDatabase db=this.getWritableDatabase();
      /*  String sql="UPDATE debits SET customer_name='"+customerNameAfter+"'where customer_name='"+customerNameBefore+"'";
        db.execSQL(sql);*/

        ContentValues contentValues=new ContentValues();
        contentValues.put("customer_name",customerNameAfter);

        long result=db.update("debits",contentValues,"customer_name='"+customerNameBefore+"'",null);

        if(result==-1)
            return false;
        else
            return true;

    }

    public boolean updateCustomerNameOnDebenturesTable(String customerNameBefore,String customerNameAfter){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("cus_name",customerNameAfter);

        long result=db.update("debentures",contentValues,"cus_name='"+customerNameBefore+"'",null);

        if(result==-1)
            return false;
        else
            return true;

    }

    public String getCustomerName(String customerName){

        String customer_name="";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select customer_name from  customers where customer_name='"+customerName+"'",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){

            customer_name=res.getString(res.getColumnIndex("customer_name"));


            res.moveToNext();
        }

        return customer_name;

    }



    public String[] getLimiteduCustomer(String customerName){

        String customerDetails[]=new String[2];
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from  customers where customer_name='"+customerName+"'",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){

            customerDetails[0]=res.getString(res.getColumnIndex("customer_name"));
            customerDetails[1]=res.getString(res.getColumnIndex("customer_phone_number"));


            res.moveToNext();
        }

        return customerDetails;

    }





    public Map<Integer,String> getAllCustomers(){

        Map<Integer,String> result =new LinkedHashMap<Integer,String>();
        Integer i=0;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from  customers ",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){

            String customer_name =res.getString(res.getColumnIndex("customer_name"));

            result.put(i,customer_name);
            i++;
            res.moveToNext();
        }

        return result;

    }

    public ArrayList<All_customers_model> getAllCustomersTOListView(){

        ArrayList<All_customers_model> result =new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from  customers  ORDER BY customer_name ASC ",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){

            String customer_name =res.getString(res.getColumnIndex("customer_name"));
            String customer_phone_number=res.getString(res.getColumnIndex("customer_phone_number"));

            result.add(new All_customers_model(customer_name,customer_phone_number));
            res.moveToNext();
        }

        return result;

    }

    public ArrayList<All_workrers_model> getAllWorkersTOListView(){

        ArrayList<All_workrers_model> result =new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from  employees  ORDER BY employee_name ASC ",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){

            String customer_name =res.getString(res.getColumnIndex("employee_name"));

            result.add(new All_workrers_model(customer_name));
            res.moveToNext();
        }

        return result;

    }


    //end employees tables


    //start depits tables



    public boolean insertNewDepite(String customer_name ,String employeeName,String description,int deservedAamount,String hand,String dateTime){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("customer_name",customer_name);
        contentValues.put("employee_name",employeeName);
        contentValues.put("description",description);
        contentValues.put("deserved_amount",deservedAamount);
        contentValues.put("hand",hand);
        contentValues.put("date",dateTime);



        long result= db.insert("debits",null,contentValues);


        if(result==-1)
            return false;
        else
            return true;


    }



    public ArrayList getAllDebits(String customername){

        ArrayList<Depits_model> arr=new ArrayList<Depits_model>();

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from  debits where customer_name='"+customername+"' ORDER BY depits_id DESC",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){

            String depits_id=res.getString(res.getColumnIndex("depits_id"));
            String customer_name=res.getString(res.getColumnIndex("customer_name"));
            String employee_name=res.getString(res.getColumnIndex("employee_name"));
            String description=res.getString(res.getColumnIndex("description"));
            String deserved_amount=res.getString(res.getColumnIndex("deserved_amount"));
            String hand=res.getString(res.getColumnIndex("hand"));
            String date=res.getString(res.getColumnIndex("date"));

            arr.add(new Depits_model(Integer.valueOf(depits_id),Integer.valueOf(deserved_amount),employee_name,customer_name,description,hand,date));

            res.moveToNext();
        }

        return arr;

    }


    public int getSumOfDebits(String customerName) {

        int sum_of_monye_paied;

        SQLiteDatabase db = this.getReadableDatabase();
        String culc = "select sum(deserved_amount) from debits where customer_name='"+customerName+"'";
        Cursor res = db.rawQuery(culc, null);

        res.moveToFirst();
        sum_of_monye_paied = res.getInt(0);
        res.close();

        return sum_of_monye_paied;
    }




    public ArrayList getLastDebit(String customername){

        ArrayList<Depits_model> arr=new ArrayList<Depits_model>();

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from  debits where depits_id=(select max(depits_id) from debits where customer_name='"+customername+"') ",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){

            String depits_id=res.getString(res.getColumnIndex("depits_id"));
            String customer_name=res.getString(res.getColumnIndex("customer_name"));
            String employee_name=res.getString(res.getColumnIndex("employee_name"));
            String description=res.getString(res.getColumnIndex("description"));
            String deserved_amount=res.getString(res.getColumnIndex("deserved_amount"));
            String hand=res.getString(res.getColumnIndex("hand"));
            String date=res.getString(res.getColumnIndex("date"));

            arr.add(new Depits_model(Integer.valueOf(depits_id),Integer.valueOf(deserved_amount),employee_name,customer_name,description,hand,date));




            res.moveToNext();
        }

        return arr;
    }


    public ArrayList getDebiteByDate(String customername,String ddate){

        ArrayList<Depits_model> arr=new ArrayList<Depits_model>();

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from  debits where  customer_name="+customername+" and date like'"+ddate+"%'",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){

            String depits_id=res.getString(res.getColumnIndex("depits_id"));
            String customer_name=res.getString(res.getColumnIndex("customer_name"));
            String employee_name=res.getString(res.getColumnIndex("employee_name"));
            String description=res.getString(res.getColumnIndex("description"));
            String deserved_amount=res.getString(res.getColumnIndex("deserved_amount"));
            String hand=res.getString(res.getColumnIndex("hand"));
            String date=res.getString(res.getColumnIndex("date"));

            arr.add(new Depits_model(Integer.valueOf(depits_id),Integer.valueOf(deserved_amount),employee_name,customer_name,description,hand,date));




            res.moveToNext();
        }

        return arr;
    }

    public int getSumOfAllDebits() {


        int sum_of_all_deebits;

        SQLiteDatabase db = this.getReadableDatabase();
        String culc = "select sum(deserved_amount) from debits";
        Cursor res = db.rawQuery(culc, null);

        res.moveToFirst();
        sum_of_all_deebits = res.getInt(0);
        res.close();




        return sum_of_all_deebits;
    }

    public boolean updateDebit(int depit_id,String workerName,String descriptionm,int deserved_amount,String hand){

        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put("employee_name",workerName);
        contentValues.put("description",descriptionm);
        contentValues.put("deserved_amount",deserved_amount);
        contentValues.put("hand",hand);
        long result=db.update("debits",contentValues,"depits_id="+depit_id+"",null);
        if(result==-1)
            return false;
        else
            return true;

    }


    public int getCountOfAllDebits(String customer_name) {


        int sum_of_all_deebits;

        SQLiteDatabase db = this.getReadableDatabase();
        String culc = "SELECT COUNT(*) FROM debits WHERE customer_name='"+customer_name+"'";
        Cursor res = db.rawQuery(culc, null);

        res.moveToFirst();
        sum_of_all_deebits = res.getInt(0);
        res.close();




        return sum_of_all_deebits;
    }

    //end depits tables


    //start debenture tables


    public boolean insertNewDebenture(String customer_name,String employee_name,int  money_paied,String date){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("cus_name",customer_name);
        contentValues.put("emp_name",employee_name);
        contentValues.put("money_paied",money_paied);
        contentValues.put("date",date);



        long result= db.insert("debentures",null,contentValues);


        if(result==-1)
            return false;
        else
            return true;


    }


    public ArrayList getAllDebenture(String customerName) {

        ArrayList<Debentures_model> arr=new ArrayList<Debentures_model>();

        SQLiteDatabase db=this.getReadableDatabase();
        //Cursor res=db.rawQuery("select * from  debentures where customer_name='"+customername+"'",null);
        Cursor res=db.rawQuery("select * from  debentures where cus_name='"+customerName+"' ORDER BY debenture_id DESC",null);

        res.moveToFirst();
        while (res.isAfterLast()==false){

            String debenture_id=res.getString(res.getColumnIndex("debenture_id"));
            String customer_name=res.getString(res.getColumnIndex("cus_name"));
            String employee_name=res.getString(res.getColumnIndex("emp_name"));
            String money_paied=res.getString(res.getColumnIndex("money_paied"));
            String date=res.getString(res.getColumnIndex("date"));

            arr.add(new Debentures_model(Integer.valueOf(debenture_id),Integer.valueOf(money_paied),employee_name,date,customer_name));




            res.moveToNext();
        }

        return arr;
    }



    public int getSumOfDebenture(String customerName) {


        int sum_of_monye_paied;

            SQLiteDatabase db = this.getReadableDatabase();
            String culc = "select sum(money_paied) from debentures where cus_name='"+customerName+"'";
            Cursor res = db.rawQuery(culc, null);

            res.moveToFirst();
                sum_of_monye_paied = res.getInt(0);
                res.close();




        return sum_of_monye_paied;
    }


    public ArrayList getLastDebenture(String customerName){

        ArrayList<Debentures_model> arr=new ArrayList<Debentures_model>();

        SQLiteDatabase db=this.getReadableDatabase();//
        //Cursor res=db.rawQuery("select * from  debentures where debenture_id=(select max(debenture_id) from debentures where customer_name='"+customerName+"')",null);
        Cursor res=db.rawQuery("select * from  debentures where debenture_id=(select max(debenture_id) from debentures where cus_name='"+customerName+"')",null);

        res.moveToFirst();
        while (res.isAfterLast()==false){

            String debenture_id=res.getString(res.getColumnIndex("debenture_id"));
            String customer_name=res.getString(res.getColumnIndex("cus_name"));
            String employee_name=res.getString(res.getColumnIndex("emp_name"));
            String money_paied=res.getString(res.getColumnIndex("money_paied"));
            String date=res.getString(res.getColumnIndex("date"));

            arr.add(new Debentures_model(Integer.valueOf(debenture_id),Integer.valueOf(money_paied),employee_name,date,customer_name));




            res.moveToNext();
        }

        return arr;
    }

    public ArrayList getDebentureByDate(String ddate){

        ArrayList<Debentures_model> arr=new ArrayList<Debentures_model>();

        SQLiteDatabase db=this.getReadableDatabase();//
        Cursor res=db.rawQuery("select * from  debentures where  date like'%"+ddate+"'",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){

            String debenture_id=res.getString(res.getColumnIndex("debenture_id"));
            String customer_name=res.getString(res.getColumnIndex("customer_name"));
            String employee_name=res.getString(res.getColumnIndex("employee_name"));
            String money_paied=res.getString(res.getColumnIndex("money_paied"));
            String date=res.getString(res.getColumnIndex("date"));

            arr.add(new Debentures_model(Integer.valueOf(debenture_id),Integer.valueOf(money_paied),employee_name,date,customer_name));




            res.moveToNext();
        }

        return arr;
    }


    public int getSumOfAllDebenture() {


        int sum_of_all_debentures;

        SQLiteDatabase db = this.getReadableDatabase();
        String culc = "select sum(money_paied) from debentures";
        Cursor res = db.rawQuery(culc, null);

        res.moveToFirst();
        sum_of_all_debentures = res.getInt(0);
        res.close();




        return sum_of_all_debentures;
    }


    public boolean updateDebenture(int debenture_id,String workerName,int money_paied){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put("emp_name",workerName);
        contentValues.put("money_paied",money_paied);
        long result=db.update("debentures",contentValues,"debenture_id="+debenture_id+"",null);
        if(result==-1)
            return false;
        else
            return true;

    }

    public int getCountOfAllDebentures(String customer_name) {

        int sum_of_all_deebits;

        SQLiteDatabase db = this.getReadableDatabase();
        String culc = "SELECT COUNT(*) FROM debentures WHERE cus_name='"+customer_name+"'";
        Cursor res = db.rawQuery(culc, null);

        res.moveToFirst();
        sum_of_all_deebits = res.getInt(0);
        res.close();




        return sum_of_all_deebits;
    }


    //end debenture tables

    //start delet

    public void deletAllEmployees(){
        SQLiteDatabase db=this.getWritableDatabase();

        String delete_employees="delete from employees";
        db.execSQL(delete_employees);


    }
    public void deletAllcustomerw(){
        SQLiteDatabase db=this.getWritableDatabase();


        String delete_customers="delete from customers";
        db.execSQL(delete_customers);

    }

    public void deletAllDebentures(){

        SQLiteDatabase db=this.getWritableDatabase();

        String delete_debentures="delete from debentures";
        db.execSQL(delete_debentures);


    }

    public void deletAllDebites(){

        SQLiteDatabase db=this.getWritableDatabase();

        String delete_debits="delete from debits";
        db.execSQL(delete_debits);

    }


    public ArrayList getAllDebitsForPDF(String customername){

        ArrayList<Depits_model> arr=new ArrayList<Depits_model>();

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from  debits where customer_name='"+customername+"' ORDER BY depits_id",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){

            String depits_id=res.getString(res.getColumnIndex("depits_id"));
            String customer_name=res.getString(res.getColumnIndex("customer_name"));
            String employee_name=res.getString(res.getColumnIndex("employee_name"));
            String description=res.getString(res.getColumnIndex("description"));
            String deserved_amount=res.getString(res.getColumnIndex("deserved_amount"));
            String hand=res.getString(res.getColumnIndex("hand"));
            String date=res.getString(res.getColumnIndex("date"));

            arr.add(new Depits_model(Integer.valueOf(depits_id),Integer.valueOf(deserved_amount),employee_name,customer_name,description,hand,date));

            res.moveToNext();
        }

        return arr;

    }


    public ArrayList getAllDebentureForPDF(String customerName) {

        ArrayList<Debentures_model> arr=new ArrayList<Debentures_model>();

        SQLiteDatabase db=this.getReadableDatabase();
        //Cursor res=db.rawQuery("select * from  debentures where customer_name='"+customername+"'",null);
        Cursor res=db.rawQuery("select * from  debentures where cus_name='"+customerName+"' ORDER BY debenture_id ",null);

        res.moveToFirst();
        while (res.isAfterLast()==false){

            String debenture_id=res.getString(res.getColumnIndex("debenture_id"));
            String customer_name=res.getString(res.getColumnIndex("cus_name"));
            String employee_name=res.getString(res.getColumnIndex("emp_name"));
            String money_paied=res.getString(res.getColumnIndex("money_paied"));
            String date=res.getString(res.getColumnIndex("date"));

            arr.add(new Debentures_model(Integer.valueOf(debenture_id),Integer.valueOf(money_paied),employee_name,date,customer_name));




            res.moveToNext();
        }

        return arr;
    }

    public void deletAllDebenturesByName(String customer_name){

        SQLiteDatabase db=this.getWritableDatabase();

        String delete_debentures="delete  from  debentures where cus_name='"+customer_name+"'";
        db.execSQL(delete_debentures);


    }

    public void deletDebenturesById(int debenture_id){

        SQLiteDatabase db=this.getWritableDatabase();

        String delete_debentures="delete  from  debentures where debenture_id="+debenture_id+"";
        db.execSQL(delete_debentures);


    }

    public void deletAllDebitesByName(String customerNname){

        SQLiteDatabase db=this.getWritableDatabase();

        String delete_debits="delete  from  debits where customer_name='"+customerNname+"'";
        db.execSQL(delete_debits);

    }

    public void deletDebeteById(int debite_id){

        SQLiteDatabase db=this.getWritableDatabase();

        String delete_debentures="delete  from  debits where depits_id="+debite_id+"";
        db.execSQL(delete_debentures);


    }

    public void deletCustomerByName(String customerNname){

        SQLiteDatabase db=this.getWritableDatabase();

        String delete_debits="delete  from  customers where customer_name='"+customerNname+"'";
        db.execSQL(delete_debits);

    }



    //end delet

/*

    public ArrayList getAllRecords(){
        ArrayList<Items_of_normalsick> arr=new ArrayList<Items_of_normalsick>();
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor res=db.rawQuery("select*from normalsick",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            String name=res.getString(res.getColumnIndex("name"));
            String gender=res.getString(res.getColumnIndex("gender"));
            String brandn_of_come=res.getString(res.getColumnIndex("brand_of_come"));
            String desirved_amount=res.getString(res.getColumnIndex("deserved_amount"));

            Integer n=Integer.valueOf(desirved_amount);



            arr.add(new Items_of_normalsick(name,gender,brandn_of_come,n));
            res.moveToNext();

        }

        return arr;



    }


 */
/*
    public ArrayList Search(String Name){
        ArrayList<Items_Of_Products> arr=new ArrayList<Items_Of_Products>();
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor res=db.rawQuery("select*from products where name like'"+Name+"%'",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            String name=res.getString(res.getColumnIndex("name"));
            String model=res.getString(res.getColumnIndex("model"));
            String price=res.getString(res.getColumnIndex("price"));
            String description=res.getString(res.getColumnIndex("description"));
            String count=res.getString(res.getColumnIndex("count"));
            // String image=res.getString(res.getColumnIndex("image"));
            String icon=res.getString(res.getColumnIndex("icon"));
            String image=res.getString(res.getColumnIndex("image"));
            String e_mail=res.getString(res.getColumnIndex("e_mail"));

            Uri getIcon= Uri.parse(icon);
            Uri getImage= Uri.parse(image);

            arr.add(new Items_Of_Products(name,model,price,description,count,e_mail,getIcon,getImage));
            res.moveToNext();

        }

        return arr;

    }

 */






}
