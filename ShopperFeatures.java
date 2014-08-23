import java.sql.Date;
import java.util.HashSet;

public class ShopperFeatures {
    public String cust_id="";
    public String cust_chain="";
    public int cust_type; //1=TRN,2=CRV,3=TST,4=RSL,5=UNK
    public String offer_id = "";
    //public String market;
    public int repeattrips=0;
    public int repeater=0; //0=false/no 1=true/yes
    //public String dept; 
    public String offer_category="";
    public int offer_quantity = 0;
    public String offer_company="";
    public float offer_value = 0; 
    public String offer_brand="";
    public Date offer_date;
    public float total_purchase_for_cust_chain_last_year=0;
    public float total_unit_price_for_cust_chain=0; //(purchaseamount/purchasequantity)
    public int total_units_purchased_per_cust_chain=0; 
    public float average_unit_price=0; //total_unit_price_for_cust_chain/total_units_purchased_per_cust_chain //aggregate
    public int total_units_purchased_by_cust_same_category=0;
    public int total_units_purchased_by_cust_same_company=0;
    public int total_units_purchased_by_cust_same_company_brand=0;
    public int total_units_purchased_by_cust_same_category_company_brand=0;
    public int total_units_purchased_by_cust_same_category_company=0;
    public float same_cat_comp_brand_purchased_by_all_total_unit_val=0; //(purchaseamount/purchasequantity), //aggregate
    public int same_cat_comp_brand_purchased_by_all_total_units=0;  //aggregate
    public float approx_ofer_val_last_year=0; //same_cat_comp_brand_purchased_by_all_total_unit_val/same_cat_comp_brand_purchased_by_all_total_units  //aggregate
    public float offer_diff=0; // (offer_value-approx_ofer_val_last_year)  //aggregate
    public int total_purchased_units_per_comp=0; //aggregate
    public int total_purchased_units_per_cate=0; //aggregate
    public int total_purchased_units_per_cate_comp=0; //aggregate
    public int total_purchased_units_per_cate_comp_brand=0; //aggregate
    public int total_purchased_units_per_comp_brand=0; //aggregate
    public int no_of_trans_records_per_cust_chain=0;
    
    public int total_no_of_repeat_trips_made_for_this_offer=0; //aggregate repeat-trips TODO
    public int no_of_cust_received_this_offer=0; //aggregate TODO
    public int no_of_cust_used_this_offer=0; //aggregate TODO
    public float percentage_of_cust_who_used_same_offer=0; //aggregate TODO no_of_cust_used_this_offer/no_of_cust_received_this_offer
    
    public float overall_spending_on_same_cat_comp_brand=0;
    public float overall_total_unit_price_on_same_cat_comp_brand=0;
    public float overall_total_units_on_same_cat_comp_brand=0;
    public float overall_per_unit_price_on_same_cat_comp_brand=0;
    public float overall_per_unit_gain_on_same_cat_comp_brand=0; 
    //non_repeater_per_unit_price_on_same_cat_comp_brand - offer_value
    
    public int total_quantity_purchased_per_cust_chain=0;
    
    public int total_times_purchased_by_cust_same_category=0;
    public int total_times_purchased_by_cust_same_company=0;
    public int total_times_purchased_by_cust_same_company_brand=0;
    public int total_times_purchased_by_cust_same_category_company_brand=0;
    public int total_times_purchased_by_cust_same_category_company=0;
    
    public float total_amount_purchased_by_cust_same_category=0;
    public float total_amount_purchased_by_cust_same_company=0;
    public float total_amount_purchased_by_cust_same_company_brand=0;
    public float total_amount_purchased_by_cust_same_category_company_brand=0;
    public float total_amount_purchased_by_cust_same_category_company=0;
    
    public int total_units_purchased_by_cust_same_brand=0;
    public float total_amount_purchased_by_cust_same_brand=0;
    public int total_times_purchased_by_cust_same_brand=0;
    
    public HashSet<Integer> frequent_weekly_shopper_score=new HashSet<Integer>();
    public HashSet<Integer> frequent_monthly_shopper_score=new HashSet<Integer>();
    
    public int no_of_distinct_category_purchased=0;
    public int no_of_distinct_company_purchased=0;
    public int no_of_distinct_brand_purchased=0;
    
    public float amount_spent_one_month_post_offer=0;
    public int quantity_bought_one_month_post_offer=0;
    public int no_of_transactions_one_month_post_offer=0;
    
    public float amount_spent_three_months_post_offer=0;
    public int quantity_bought_three_months_post_offer=0;
    public int no_of_transactions_three_months_post_offer=0;
    
    
    public String printData(){
        StringBuilder sb = new StringBuilder();
        sb.append(cust_id); sb.append(","); //1
        sb.append(cust_chain); sb.append(","); //2
        sb.append(cust_type); sb.append(","); //3
        sb.append(offer_id); sb.append(","); //4
        //sb.append(market); sb.append(",");
        sb.append(repeattrips); sb.append(","); //5
        sb.append(repeater); sb.append(","); //6
        sb.append(offer_category); sb.append(","); //7
        sb.append(offer_quantity); sb.append(","); //8
        sb.append(offer_company); sb.append(","); //9
        sb.append(offer_value); sb.append(","); //10
        sb.append(offer_brand); sb.append(","); //11
        sb.append(total_purchase_for_cust_chain_last_year); sb.append(","); //12
        sb.append(total_unit_price_for_cust_chain); sb.append(","); //13
        sb.append(total_units_purchased_per_cust_chain); sb.append(","); //14
        sb.append(average_unit_price); sb.append(","); //15
        sb.append(total_units_purchased_by_cust_same_category); sb.append(","); //16
        sb.append(total_units_purchased_by_cust_same_company); sb.append(","); //17
        sb.append(total_units_purchased_by_cust_same_company_brand); sb.append(","); //18
        sb.append(total_units_purchased_by_cust_same_category_company_brand); sb.append(","); //19
        sb.append(total_units_purchased_by_cust_same_category_company); sb.append(","); //20
        sb.append(same_cat_comp_brand_purchased_by_all_total_unit_val); sb.append(","); //21
        sb.append(same_cat_comp_brand_purchased_by_all_total_units); sb.append(","); //22
        sb.append(approx_ofer_val_last_year); sb.append(","); //23
        sb.append(offer_diff); sb.append(","); //24
        sb.append(total_purchased_units_per_comp); sb.append(","); //25
        sb.append(total_purchased_units_per_cate); sb.append(","); //26
        sb.append(total_purchased_units_per_cate_comp); sb.append(","); //27
        sb.append(total_purchased_units_per_cate_comp_brand); sb.append(","); //28
        sb.append(total_purchased_units_per_comp_brand); sb.append(","); //29
        sb.append(no_of_trans_records_per_cust_chain); sb.append(",");//30
        sb.append(total_no_of_repeat_trips_made_for_this_offer); sb.append(",");//31
        sb.append(no_of_cust_received_this_offer); sb.append(",");//32
        sb.append(no_of_cust_used_this_offer); sb.append(",");//33
        sb.append(percentage_of_cust_who_used_same_offer); sb.append(",");//34
        sb.append(overall_spending_on_same_cat_comp_brand); sb.append(",");//35
        sb.append(overall_total_unit_price_on_same_cat_comp_brand); sb.append(",");//36
        sb.append(overall_total_units_on_same_cat_comp_brand); sb.append(",");//37
        sb.append(overall_per_unit_price_on_same_cat_comp_brand); sb.append(",");//38
        sb.append(overall_per_unit_gain_on_same_cat_comp_brand); sb.append(",");//39
        sb.append(total_quantity_purchased_per_cust_chain);  sb.append(","); //40
        
        sb.append(total_times_purchased_by_cust_same_category);  sb.append(","); //41
        sb.append(total_times_purchased_by_cust_same_company);  sb.append(","); //42
        sb.append(total_times_purchased_by_cust_same_company_brand);  sb.append(","); //43
        sb.append(total_times_purchased_by_cust_same_category_company_brand);  sb.append(","); //44
        sb.append(total_times_purchased_by_cust_same_category_company);  sb.append(","); //45
        sb.append(total_amount_purchased_by_cust_same_category);  sb.append(","); //46
        sb.append(total_amount_purchased_by_cust_same_company);  sb.append(","); //47
        sb.append(total_amount_purchased_by_cust_same_company_brand);  sb.append(","); //48
        sb.append(total_amount_purchased_by_cust_same_category_company_brand);  sb.append(","); //49
        sb.append(total_amount_purchased_by_cust_same_category_company); sb.append(","); //50
        
        sb.append(total_units_purchased_by_cust_same_brand); sb.append(","); //51
        sb.append(total_times_purchased_by_cust_same_brand);  sb.append(","); //52
        sb.append(total_amount_purchased_by_cust_same_brand); sb.append(","); //53
        
        sb.append(frequent_weekly_shopper_score.size()); sb.append(","); //54
        sb.append(frequent_monthly_shopper_score.size()); sb.append(","); //55
        
        sb.append(no_of_distinct_category_purchased); sb.append(","); //56
        sb.append(no_of_distinct_company_purchased); sb.append(","); //57
        sb.append(no_of_distinct_brand_purchased); sb.append(","); //58
        
        sb.append(amount_spent_one_month_post_offer); sb.append(","); //59
        sb.append(quantity_bought_one_month_post_offer); sb.append(","); //60
        sb.append(no_of_transactions_one_month_post_offer); sb.append(","); //61
        
        sb.append(amount_spent_three_months_post_offer); sb.append(","); //62
        sb.append(quantity_bought_three_months_post_offer); sb.append(","); //63
        sb.append(no_of_transactions_three_months_post_offer); //64
        
        //65
        //66
        //67
        
        
        //12 30 40 16 17 18 19 20 51 41 42 43 44 45 52 46 47 48 49 50 53 54 55 59 60 61 62 63 64 6 ->65%
        //12 46 47 48 49 50 53 59 60 61 62 63 64 6 ->69%
        //12 46 47 48 49 50 53 59 60 61 6 ->69%
        //12 46 47 48 49 50 53 6 ->68%
        //12 46 48 49 6->69.43% 
        //30 41 42 43 44 45 6
        //12 46 47 48 49 50 6
        //40 16 17 18 19 20 6
        //30 16 17 18 19 20 39 34 6
        //12 30 40 16 17 18 19 20 39 35 34 6
        //12 30 40 16 17 18 19 20 6
        //12 16 18 19 6
        //12 30 16 18 19 6 //68.42 0.5856
        //12 14 15 16 18 19 20 25 28 30
        //12 40 30 16 17 18 19 20
        //12 16 17 18 19 20 25 26 27 28 29 30
        //12 30 16 17
        
        //12 30 40 31 34 16 17 18 19 20
        
        return sb.toString();
    }
}
