import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class AcquiredValueShopperChallange {

    public static HashMap<String,ShopperFeatures> allCustMap=new HashMap<String,ShopperFeatures>();
    public static HashMap<String,Offer> offers=new HashMap<String,Offer>();
    public static HashMap<String,Offer> offersByCatCompBrand=new HashMap<String,Offer>();
    public static HashMap<String,HashSet<String>> categoryShopper = new HashMap<String,HashSet<String>>(); 
    public static HashMap<String,HashSet<String>> companyShopper = new HashMap<String,HashSet<String>>();
    public static HashMap<String,HashSet<String>> brandShopper = new HashMap<String,HashSet<String>>();
    public static String encoding = "UTF-8";
    
    public static void main(String [] args) throws IOException {
        
        System.out.println("=================================================================================");
        readOffersData();
        System.out.println("=================================================================================");
        readTrainCustHistory();
        System.out.println("=================================================================================");
        readTestCustHistory();
        System.out.println("=================================================================================");
        readTransactionHistory();
        System.out.println("=================================================================================");
        populateAggregateFunctionsOnOffers();
        System.out.println("=================================================================================");
        populateAggregateFunctionsOnCust();
        System.out.println("=================================================================================");
        processDistinctShoppers();
        System.out.println("=================================================================================");
        dumpAllCustFeaturesData();
        System.out.println("=================================================================================");
    }

    private static void processDistinctShoppers() {
        System.out.println("Started processing distinct shoppers");
        long start = System.nanoTime();
        
        Iterator<Entry<String, HashSet<String>>> it1 = categoryShopper.entrySet().iterator();
        System.out.println("categoryShopper Map Size: " + categoryShopper.size());
        while (it1.hasNext()) {
            Map.Entry<String, HashSet<String>> pairs = (Map.Entry<String, HashSet<String>>)it1.next();
            HashSet<String> sh1 = pairs.getValue();
            Iterator<String> it2 = sh1.iterator();
            while (it2.hasNext()){
                allCustMap.get(it2.next()).no_of_distinct_category_purchased++;
            }
        }
        
        it1 = companyShopper.entrySet().iterator();
        System.out.println("companyShopper Map Size: " + companyShopper.size());
        while (it1.hasNext()) {
            Map.Entry<String, HashSet<String>> pairs = (Map.Entry<String, HashSet<String>>)it1.next();
            HashSet<String> sh1 = pairs.getValue();
            Iterator<String> it2 = sh1.iterator();
            while (it2.hasNext()){
                allCustMap.get(it2.next()).no_of_distinct_company_purchased++;
            }
        }
        
        it1 = brandShopper.entrySet().iterator();
        System.out.println("brandShopper Map Size: " + brandShopper.size());
        while (it1.hasNext()) {
            Map.Entry<String, HashSet<String>> pairs = (Map.Entry<String, HashSet<String>>)it1.next();
            HashSet<String> sh1 = pairs.getValue();
            Iterator<String> it2 = sh1.iterator();
            while (it2.hasNext()){
                allCustMap.get(it2.next()).no_of_distinct_brand_purchased++;
            }
        }
        
        long time = System.nanoTime() - start;
        System.out.println("Finished populating aggregate functions for Offers in " + time/1e9 + " seconds");
    }

    private static void populateAggregateFunctionsOnOffers() {
        System.out.println("Started populating aggregate functions for Offers");
        long start = System.nanoTime();
        int counter = 0;
        Iterator<Entry<String, Offer>> it1 = offers.entrySet().iterator();
        int mapsize = offers.size();
        System.out.println("Map Size: " + mapsize);
        while (it1.hasNext()) {
            Map.Entry<String, Offer> pairs = (Map.Entry<String, Offer>)it1.next();
            Offer o1 = pairs.getValue();
            
            if(o1.no_of_cust_received_this_offer==0)
                o1.percentage_of_cust_who_used_same_offer = 0;
            else
                o1.percentage_of_cust_who_used_same_offer = ((float)o1.no_of_cust_used_this_offer / o1.no_of_cust_received_this_offer)*100;
            if(o1.overall_total_units_on_same_cat_comp_brand==0)
                o1.overall_per_unit_price_on_same_cat_comp_brand = o1.offer_value;
            else{
                o1.overall_per_unit_price_on_same_cat_comp_brand = o1.overall_total_unit_price_on_same_cat_comp_brand / o1.overall_total_units_on_same_cat_comp_brand;
                o1.overall_per_unit_gain_on_same_cat_comp_brand = o1.overall_per_unit_price_on_same_cat_comp_brand - o1.offer_value;
            }
            
            Iterator<Entry<String, Offer>> it2 = offers.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Offer> pairs2 = (Map.Entry<String, Offer>)it2.next();
                Offer o2 = pairs2.getValue();
                if(o1.offer_company.equals(o2.offer_company)) 
                    o1.total_purchased_units_by_comp+=o2.total_purchased_units_by_cate_comp_brand;
                if(o1.offer_category.equals(o2.offer_category)) 
                    o1.total_purchased_units_by_cate+=o2.total_purchased_units_by_cate_comp_brand;
                if((o1.offer_category.equals(o2.offer_category))&&(o1.offer_company.equals(o2.offer_company))) 
                    o1.total_purchased_units_by_cate_comp+=o2.total_purchased_units_by_cate_comp_brand;
                if((o1.offer_company.equals(o2.offer_company))&&(o1.offer_brand.equals(o2.offer_brand))) 
                    o1.total_purchased_units_by_comp_brand+=o2.total_purchased_units_by_cate_comp_brand;
            }
            counter++;
            System.out.println("Finished with " + counter + " Offers");
        }
        long time = System.nanoTime() - start;
        System.out.println("Finished populating aggregate functions for Offers in " + time/1e9 + " seconds");
    }

    private static void populateAggregateFunctionsOnCust() {
        System.out.println("Started populating aggregate functions for Cust");
        long start = System.nanoTime();
        int counter = 0;
        Iterator<Entry<String, ShopperFeatures>> it = allCustMap.entrySet().iterator();
        int mapsize = allCustMap.size();
        System.out.println("Map Size: " + mapsize);
        while (it.hasNext()) {
            Map.Entry<String, ShopperFeatures> pairs = (Map.Entry<String, ShopperFeatures>)it.next();
            ShopperFeatures sf = pairs.getValue();
            if (sf.total_units_purchased_per_cust_chain>0)
                sf.average_unit_price = sf.total_unit_price_for_cust_chain/sf.total_units_purchased_per_cust_chain;
            Offer o1 = offers.get(sf.offer_id);
            if (o1!=null){
                sf.same_cat_comp_brand_purchased_by_all_total_unit_val=o1.total_non_offer_purchased_unit_price_last_year;
                sf.same_cat_comp_brand_purchased_by_all_total_units=o1.total_non_offer_purchased_units_last_year;
                if (sf.same_cat_comp_brand_purchased_by_all_total_units>0) 
                    sf.approx_ofer_val_last_year=sf.same_cat_comp_brand_purchased_by_all_total_unit_val/sf.same_cat_comp_brand_purchased_by_all_total_units;
                sf.offer_diff=sf.offer_value-sf.approx_ofer_val_last_year;
                sf.total_purchased_units_per_comp = o1.total_purchased_units_by_comp;
                sf.total_purchased_units_per_cate = o1.total_purchased_units_by_cate;
                sf.total_purchased_units_per_cate_comp = o1.total_purchased_units_by_cate_comp;
                sf.total_purchased_units_per_cate_comp_brand = o1.total_purchased_units_by_cate_comp_brand;
                sf.total_purchased_units_per_comp_brand = o1.total_purchased_units_by_comp_brand;
                
                sf.total_no_of_repeat_trips_made_for_this_offer = o1.total_no_of_repeat_trips_made_for_this_offer;
                sf.no_of_cust_received_this_offer = o1.no_of_cust_received_this_offer;
                sf.no_of_cust_used_this_offer = o1.no_of_cust_used_this_offer;
                sf.percentage_of_cust_who_used_same_offer = o1.percentage_of_cust_who_used_same_offer;
                
                sf.overall_per_unit_gain_on_same_cat_comp_brand = o1.overall_per_unit_gain_on_same_cat_comp_brand;
                sf.overall_per_unit_price_on_same_cat_comp_brand = o1.overall_per_unit_price_on_same_cat_comp_brand;
                sf.overall_spending_on_same_cat_comp_brand = o1.overall_spending_on_same_cat_comp_brand;
                sf.overall_total_unit_price_on_same_cat_comp_brand = o1.overall_total_unit_price_on_same_cat_comp_brand;
                sf.overall_total_units_on_same_cat_comp_brand = o1.overall_total_units_on_same_cat_comp_brand;
            }
            counter++;
            if (counter%25000 == 0)
                System.out.println("Finished with " + counter + " Records");
        }
        
        long time = System.nanoTime() - start;
        System.out.println("Finished populating aggregate functions for Cust in " + time/1e9 + " seconds");
    }

    private static void readOffersData() throws IOException {
        long start = System.nanoTime();
        int counter = 0;
        BufferedReader reader = null;
        System.out.println("Started reading offers.csv");
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\kaggle\\01\\offers.csv"), encoding));
            String header = reader.readLine();
            System.out.println(header);
            
            for (String line; (line = reader.readLine()) != null;) {
                String[] tokens = line.split(",");
                //offer,category,quantity,company,offervalue,brand
                Offer o1 = new Offer();
                o1.offer_id=tokens[0];
                o1.offer_category=tokens[1];
                o1.offer_quantity=Integer.parseInt(tokens[2]);
                o1.offer_company=tokens[3];
                o1.offer_value=Float.parseFloat(tokens[4]);
                o1.offer_brand=tokens[5];
                offers.put(o1.offer_id, o1);
                offersByCatCompBrand.put(o1.offer_category+"X"+o1.offer_company+"X"+o1.offer_brand, o1);
                counter++;
                if (counter%10 == 0)
                    System.out.println("Read " + counter + " Records");
            }
        }finally{
            reader.close();
        }
        System.out.println("Finished reding. Read " + counter + " Records");
        long time = System.nanoTime() - start;
        System.out.println("Reading Took " + time/1e9 + " seconds");
        
    }

    public static void readTrainCustHistory() throws IOException{
        
        long start = System.nanoTime();
        int counter = 0, repeater_cnt=0, non_repeater_cnt=0;
        BufferedReader reader = null;
        System.out.println("Started reading trainHistory.csv");
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\kaggle\\01\\trainHistory.csv"), encoding));
            String header = reader.readLine();
            System.out.println(header);
            
            for (String line; (line = reader.readLine()) != null;) {
                String[] tokens = line.split(",");
                //id,chain,offer,market,repeattrips,repeater,offerdate
                ShopperFeatures sf = new ShopperFeatures();
                sf.cust_id=tokens[0];
                sf.cust_chain=tokens[1];
                sf.offer_id=tokens[2];
                sf.offer_date=Date.valueOf(tokens[6]);
                sf.repeattrips=Integer.parseInt(tokens[4]);
                if (tokens[5].equalsIgnoreCase("t")){
                    sf.repeater=1;
                    repeater_cnt++;
                    if (repeater_cnt%5>1) sf.cust_type=1; //"TRN";
                    if (repeater_cnt%5==1) sf.cust_type=2; //"CRV";
                    if (repeater_cnt%5==0) sf.cust_type=3; //"TST";
                } else if (tokens[5].equalsIgnoreCase("f")){
                    sf.repeater=0;
                    non_repeater_cnt++;
                    if (non_repeater_cnt%5>1) sf.cust_type=1; //"TRN";
                    if (non_repeater_cnt%5==1) sf.cust_type=2; //"CRV";
                    if (non_repeater_cnt%5==0) sf.cust_type=3; //"TST";
                } else {
                    sf.cust_type=5; //"UNK";
                }
                Offer o1 = offers.get(sf.offer_id);
                if(o1!=null){
                    o1.no_of_cust_received_this_offer++;
                    if (sf.repeater==1){
                        o1.no_of_cust_used_this_offer++;
                        o1.total_no_of_repeat_trips_made_for_this_offer = o1.total_no_of_repeat_trips_made_for_this_offer + sf.repeattrips;
                    }
                }
                allCustMap.put(formHashMapKey(sf.cust_id, sf.cust_chain), sf);
                counter++;
                if (counter%25000 == 0)
                    System.out.println("Read " + counter + " Records");
            }
        }finally{
            reader.close();
        }
        System.out.println("Finished reding. Read " + counter + " Records");
        long time = System.nanoTime() - start;
        System.out.println("Reading Took " + time/1e9 + " seconds");
    }

    public static void readTestCustHistory() throws IOException{
        long start = System.nanoTime();
        int counter = 0;
        BufferedReader reader = null;
        System.out.println("Started reading testHistory.csv");
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\kaggle\\01\\testHistory.csv"), encoding));
            String header = reader.readLine();
            System.out.println(header);
            
            for (String line; (line = reader.readLine()) != null;) {
                String[] tokens = line.split(",");
                //id,chain,offer,market,offerdate
                ShopperFeatures sf = new ShopperFeatures();
                sf.cust_id=tokens[0];
                sf.cust_chain=tokens[1];
                sf.offer_id=tokens[2];
                sf.offer_date=Date.valueOf(tokens[4]);
                sf.cust_type=4; //"RSL";
                allCustMap.put(formHashMapKey(sf.cust_id, sf.cust_chain), sf);
                counter++;
                if (counter%25000 == 0)
                    System.out.println("Read " + counter + " Records");
            }
        }finally{
            reader.close();
        }
        System.out.println("Finished reding. Read " + counter + " Records");
        long time = System.nanoTime() - start;
        System.out.println("Reading Took " + time/1e9 + " seconds");        
    }
    
    public static void readTransactionHistory() throws IOException, FileNotFoundException{
        
        long start = System.nanoTime();
        int counter = 0;
        BufferedReader reader = null;
        //String currentCustID=null, currentCustChain=null, prevCustID=null, prevCustChain=null;
        System.out.println("Started reading transactions.csv");
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\kaggle\\01\\transactions.csv"), encoding));
            //id[0],chain[1],dept[2],category[3],company[4],brand[5],date[6],productsize[7],productmeasure[8],purchasequantity[9],purchaseamount[10]
            String header = reader.readLine();
            System.out.println(header);
            for (String line; (line = reader.readLine()) != null;) {
                String[] tokens = line.split(",");
                String t_custId = tokens[0];
                String t_cust_chain = tokens[1];
                
                ShopperFeatures sf = allCustMap.get(formHashMapKey(t_custId, t_cust_chain));
                if (sf==null){
                    //create and insert new cust-chain here
                    ShopperFeatures tsf = new ShopperFeatures();
                    tsf.cust_id=t_custId;
                    tsf.cust_chain=t_cust_chain;
                    tsf.cust_type=5; //"UNK";
                    allCustMap.put(formHashMapKey(tsf.cust_id, tsf.cust_chain), tsf);
                    sf = tsf;
                }
                
                //process transaction
                sf.offer_category = tokens[3]; //unwanted TODO
                sf.offer_company = tokens[4]; //unwanted TODO
                sf.offer_brand = tokens[5]; //unwanted TODO
                
                if(Float.parseFloat(tokens[9])>0 && Float.parseFloat(tokens[10])>0){
                    sf.total_purchase_for_cust_chain_last_year+=Float.parseFloat(tokens[10]);
                    sf.total_unit_price_for_cust_chain+=(Float.parseFloat(tokens[10])/Float.parseFloat(tokens[9]));
                    sf.total_units_purchased_per_cust_chain++;
                    sf.total_quantity_purchased_per_cust_chain+=Integer.parseInt(tokens[9]);
                }
                
                if (sf.offer_id != null)
                {
                    Offer temp_offer = offers.get(sf.offer_id);
                    if (temp_offer!=null){
                        sf.offer_quantity = temp_offer.offer_quantity;
                        sf.offer_value = temp_offer.offer_value;
                        if(sf.offer_brand.equals(temp_offer.offer_brand)) 
                            if(Float.parseFloat(tokens[9])>0 && Float.parseFloat(tokens[10])>0){
                                sf.total_units_purchased_by_cust_same_brand+=Float.parseFloat(tokens[9]);
                                sf.total_amount_purchased_by_cust_same_brand+=Float.parseFloat(tokens[10]);
                                sf.total_times_purchased_by_cust_same_brand++;
                            }
                        if(sf.offer_category.equals(temp_offer.offer_category)) 
                            if(Float.parseFloat(tokens[9])>0 && Float.parseFloat(tokens[10])>0){
                                sf.total_units_purchased_by_cust_same_category+=Float.parseFloat(tokens[9]);
                                sf.total_amount_purchased_by_cust_same_category+=Float.parseFloat(tokens[10]);
                                sf.total_times_purchased_by_cust_same_category++;
                            }
                        if(sf.offer_company.equals(temp_offer.offer_company))
                            if(Float.parseFloat(tokens[9])>0 && Float.parseFloat(tokens[10])>0){
                                sf.total_units_purchased_by_cust_same_company+=Float.parseFloat(tokens[9]);
                                sf.total_amount_purchased_by_cust_same_company+=Float.parseFloat(tokens[10]);
                                sf.total_times_purchased_by_cust_same_company++;
                            }
                        if((sf.offer_category.equals(temp_offer.offer_category))&&(sf.offer_company.equals(temp_offer.offer_company)))
                            if(Float.parseFloat(tokens[9])>0 && Float.parseFloat(tokens[10])>0){
                                sf.total_units_purchased_by_cust_same_category_company+=Float.parseFloat(tokens[9]);
                                sf.total_amount_purchased_by_cust_same_category_company+=Float.parseFloat(tokens[10]);
                                sf.total_times_purchased_by_cust_same_category_company++;
                            }
                        if((sf.offer_brand.equals(temp_offer.offer_brand))&&(sf.offer_company.equals(temp_offer.offer_company)))
                            if(Float.parseFloat(tokens[9])>0 && Float.parseFloat(tokens[10])>0){
                                sf.total_units_purchased_by_cust_same_company_brand+=Float.parseFloat(tokens[9]);
                                sf.total_amount_purchased_by_cust_same_company_brand+=Float.parseFloat(tokens[10]);
                                sf.total_times_purchased_by_cust_same_company_brand++;
                            }
                        if((sf.offer_category.equals(temp_offer.offer_category))&&(sf.offer_company.equals(temp_offer.offer_company))&&(sf.offer_brand.equals(temp_offer.offer_brand)))
                            if(Float.parseFloat(tokens[9])>0 && Float.parseFloat(tokens[10])>0){
                                sf.total_units_purchased_by_cust_same_category_company_brand+=Float.parseFloat(tokens[9]);
                                sf.total_amount_purchased_by_cust_same_category_company_brand+=Float.parseFloat(tokens[10]);
                                sf.total_times_purchased_by_cust_same_category_company_brand++;
                            }
                        
                        if (Float.parseFloat(tokens[9])>0 && Float.parseFloat(tokens[10])>0) temp_offer.total_purchased_units_by_cate_comp_brand+=Integer.parseInt(tokens[9]);
                    }
                }
                
                //non repeater purchases 1. money spent 2. units bought 3. aggregated units 4. no of times 4. average unit price
                //
//                if(Float.parseFloat(tokens[9])>0 && Float.parseFloat(tokens[10])>0){
//                    temp_offer.total_non_offer_purchased_unit_price_last_year+=(Float.parseFloat(tokens[10])/Float.parseFloat(tokens[9]));
//                    temp_offer.total_non_offer_purchased_units_last_year++;
//                }
                
                //total purchases 1. money spent 2. units bought 3. aggregated units 4. no of times 4. average unit price
                
                
                sf.no_of_trans_records_per_cust_chain++;
                
//                HashSet<String> shoppers = categoryShopper.get(tokens[3]);
//                if(shoppers==null){
//                    HashSet<String> sh1 = new HashSet<String>();
//                    sh1.add(formHashMapKey(sf.cust_id, sf.cust_chain));
//                    categoryShopper.put(tokens[3], sh1);
//                }else{
//                    shoppers.add(formHashMapKey(sf.cust_id, sf.cust_chain));
//                }
//                
//                shoppers = companyShopper.get(tokens[4]);
//                if(shoppers==null){
//                    HashSet<String> sh1 = new HashSet<String>();
//                    sh1.add(formHashMapKey(sf.cust_id, sf.cust_chain));
//                    companyShopper.put(tokens[4], sh1);
//                }else{
//                    shoppers.add(formHashMapKey(sf.cust_id, sf.cust_chain));
//                }
//                
//                shoppers = brandShopper.get(tokens[5]);
//                if(shoppers==null){
//                    HashSet<String> sh1 = new HashSet<String>();
//                    sh1.add(formHashMapKey(sf.cust_id, sf.cust_chain));
//                    brandShopper.put(tokens[5], sh1);
//                }else{
//                    shoppers.add(formHashMapKey(sf.cust_id, sf.cust_chain));
//                }
                
                //Date operations
                try{
                    Date transaction_date = Date.valueOf(tokens[6]);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(transaction_date);
                    sf.frequent_weekly_shopper_score.add(cal.get(Calendar.WEEK_OF_YEAR));
                    sf.frequent_monthly_shopper_score.add(cal.get(Calendar.MONTH));
                    int shopping_day=cal.get(Calendar.DAY_OF_YEAR);
                    cal.setTime(sf.offer_date);
                    int offer_day=cal.get(Calendar.DAY_OF_YEAR);
                    if(shopping_day>=offer_day && (shopping_day-offer_day)<=30){
                        if(Float.parseFloat(tokens[9])>0 && Float.parseFloat(tokens[10])>0){
                            sf.amount_spent_one_month_post_offer+=Float.parseFloat(tokens[10]);
                            sf.quantity_bought_one_month_post_offer+=Float.parseFloat(tokens[9]);
                            sf.no_of_transactions_one_month_post_offer++;
                        }
                    }
                    if(shopping_day>=offer_day && (shopping_day-offer_day)<=90){
                        if(Float.parseFloat(tokens[9])>0 && Float.parseFloat(tokens[10])>0){
                            sf.amount_spent_three_months_post_offer+=Float.parseFloat(tokens[10]);
                            sf.quantity_bought_three_months_post_offer+=Float.parseFloat(tokens[9]);
                            sf.no_of_transactions_three_months_post_offer++;
                        }
                    }
                }catch(Exception e){
                    //ignore
                }
                
                Offer of1 = offersByCatCompBrand.get(tokens[3]+"X"+tokens[4]+"X"+tokens[5]);
                
                if(of1!=null){
                    if(Float.parseFloat(tokens[9])>0 && Float.parseFloat(tokens[10])>0){
                        of1.overall_spending_on_same_cat_comp_brand+=Float.parseFloat(tokens[10]);
                        of1.overall_total_unit_price_on_same_cat_comp_brand+=(Float.parseFloat(tokens[10])/Float.parseFloat(tokens[9]));
                        of1.overall_total_units_on_same_cat_comp_brand++;
                    }
                }

                //end processing transaction
                
                counter++;
                if (counter%10000000 == 0)
                    System.out.println("Read " + counter + " Records");
            }
        }finally{
            reader.close();
        }
        System.out.println("Finished reding. Read " + counter + " Records");
        long time = System.nanoTime() - start;
        System.out.println("Reading Took " + (time/1e9)/60 + " minutes");
    }
    
    public static void dumpAllCustFeaturesData() throws IOException{
    
        long start = System.nanoTime();
        
        Iterator<Entry<String, ShopperFeatures>> it = allCustMap.entrySet().iterator();
        System.out.println("Map Size: " + allCustMap.size());
        int counter = 0;
        int mapsize=allCustMap.size();
        BufferedWriter writer = null, trn_writer = null, crv_writer = null, tst_writer = null, rsl_writer = null, unk_writer = null;
        try{
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\kaggle\\01\\allCustFeatures01.csv"), encoding));
            trn_writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\kaggle\\01\\trnCustFeatures01.csv"), encoding));
            crv_writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\kaggle\\01\\crvCustFeatures01.csv"), encoding));
            tst_writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\kaggle\\01\\tstCustFeatures01.csv"), encoding));
            rsl_writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\kaggle\\01\\rslCustFeatures01.csv"), encoding));
            unk_writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\kaggle\\01\\unkCustFeatures01.csv"), encoding));
            while (it.hasNext()) {
                Map.Entry<String, ShopperFeatures> pairs = (Map.Entry<String, ShopperFeatures>)it.next();
                String line = pairs.getValue().printData();
                writer.write(line);
                writer.newLine(); 
                if (pairs.getValue().cust_type==1){
                    trn_writer.write(line);
                    trn_writer.newLine();
                }
                if (pairs.getValue().cust_type==2){
                    crv_writer.write(line);
                    crv_writer.newLine();
                }
                if (pairs.getValue().cust_type==3){
                    tst_writer.write(line);
                    tst_writer.newLine();
                }
                if (pairs.getValue().cust_type==4){
                    rsl_writer.write(line);
                    rsl_writer.newLine();
                }
                if (pairs.getValue().cust_type==5){
                    unk_writer.write(line);
                    unk_writer.newLine();
                }
                counter++;
                //if(counter!=mapsize) 
                
                if (counter%25000 == 0)
                    System.out.println("Dumped " + counter + " Records");
                //it.remove(); // avoids a ConcurrentModificationException
            }
        }finally{
            writer.close();
            trn_writer.close();
            crv_writer.close();
            tst_writer.close();
            rsl_writer.close();
            unk_writer.close();
        }
        System.out.println("Finished dumping. Dumped " + counter + " Records");
        long time = System.nanoTime() - start;
        System.out.println("Dumping Took " + time/1e9 + " seconds");
        
    }
    
    public static String formHashMapKey(String custId, String custChain){
        return custId + "X" + custChain;
    }
    
    public static String getCustIdFromKey(String key){
        String custId=null;
        
        return custId;
    }
    
    public static String getCustChainFromKey(String key){
        String custChain=null;
        
        return custChain;
    }
}

