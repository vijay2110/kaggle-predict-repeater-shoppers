public class Offer {
    public String offer_id;
    public String offer_category;
    public int offer_quantity = 0;
    public String offer_company;
    public float offer_value = 0;
    public String offer_brand;
    
    public int total_non_offer_purchased_units_last_year = 0;
    public float total_non_offer_purchased_unit_price_last_year = 0;
    
    public int total_purchased_units_by_cate_comp_brand = 0;
    public int total_purchased_units_by_comp = 0;
    public int total_purchased_units_by_comp_brand = 0;
    public int total_purchased_units_by_cate = 0;
    public int total_purchased_units_by_cate_comp = 0;
    
    public int no_of_cust_received_this_offer=0;
    public int no_of_cust_used_this_offer=0;
    public int total_no_of_repeat_trips_made_for_this_offer=0;
    public float percentage_of_cust_who_used_same_offer=0;
    
    public float overall_spending_on_same_cat_comp_brand=0;
    public float overall_total_unit_price_on_same_cat_comp_brand=0;
    public int overall_total_units_on_same_cat_comp_brand=0;
    public float overall_per_unit_price_on_same_cat_comp_brand=0;
    public float overall_per_unit_gain_on_same_cat_comp_brand=0; 
    //non_repeater_per_unit_price_on_same_cat_comp_brand - offer_value
}
