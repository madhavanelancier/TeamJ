package com.elancier.team_j

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PickupModel {
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("dashboard")
    @Expose
    var dashboard: String? = null
    @SerializedName("response")
    @Expose
    var response: List<PickupModel> = ArrayList()

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("pickup_id")
    @Expose
    var pickup_id: String? = null

    @SerializedName("check_status")
    @Expose
    var check_status: Int? = null

    @SerializedName("in_time")
    @Expose
    var in_time: String? = null



    @SerializedName("balance")
    @Expose
    var balance: String? = null

    @SerializedName("docket_id")
    @Expose
    var docket_id: String? = null

    @SerializedName("unit_length")
    @Expose
    var unit_length: String? = null

    @SerializedName("unit_breadth")
    @Expose
    var unit_breadth: String? = null

    @SerializedName("unit_height")
    @Expose
    var unit_height: String? = null

    @SerializedName("total_kg")
    @Expose
    var total_kg: String? = null

    @SerializedName("box_no")
    @Expose
    var box_no: String? = null

    @SerializedName("application_from_date")
    @Expose
    var application_from_date: String? = null

    @SerializedName("application_to_date")
    @Expose
    var application_to_date: String? = null

    @SerializedName("number_of_day")
    @Expose
    var number_of_day: String? = null

    @SerializedName("approve_date")
    @Expose
    var approve_date: String? = null

    @SerializedName("reject_date")
    @Expose
    var reject_date: String? = null

    @SerializedName("approve_by")
    @Expose
    var approve_by: PickupModel? = null

    @SerializedName("employee")
    @Expose
    var employee: PickupModel? = null

    @SerializedName("first_name")
    @Expose
    var first_name: String? = null

    @SerializedName("reject_by")
    @Expose
    var reject_by: PickupModel? = null

    @SerializedName("purpose")
    @Expose
    var purpose: String? = null

    @SerializedName("leave_type")
    @Expose
    var leave_type: String? = null

    @SerializedName("leave_type_name")
    @Expose
    var leave_type_name: String? = null



    @SerializedName("vehicle_details")
    @Expose
    var vehicle_details: PickupModel? = null

    @SerializedName("vechile_number")
    @Expose
    var vechile_number: String? = null


    @SerializedName("scanned")
    @Expose
    var scanned: Int? = null

    @SerializedName("deps")
    @Expose
    var deps: Int? = null

    @SerializedName("reason")
    @Expose
    var deps_reason: String? = null

    @SerializedName("scan_date")
    @Expose
    var scan_date: String? = null



    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("branch_id")
    @Expose
    var branchId: String? = null
    @SerializedName("pickups")
    @Expose
    var pickups: ArrayList<PickupModel>? = null

    @SerializedName("consignee_name")
    @Expose
    var consignee_name: String? = null

    @SerializedName("date")
    @Expose
    var fdate: String? = null

    @SerializedName("to_date")
    @Expose
    var to_date: String? = null

    @SerializedName("consignee_mobile")
    @Expose
    var consignee_mobile: String? = null

    @SerializedName("consignee_address")
    @Expose
    var consignee_address: String? = null
    @SerializedName("customer_type")
    @Expose
    var customerType: String? = null
    @SerializedName("customer")
    @Expose
    var customer: String? = null
    @SerializedName("docket_details")
    @Expose
    var docket_details: List<PickupModel>? = null
    @SerializedName("customer_details")
    @Expose
    var customer_details: PickupModel? = null
    @SerializedName("pickup_location")
    @Expose
    var pickupLocation: String? = null
    @SerializedName("pickup_pincode")
    @Expose
    var pickupPincode: String? = null
    @SerializedName("pickup_date")
    @Expose
    var pickupDate: String? = null
    @SerializedName("pickup_time")
    @Expose
    var pickupTime: String? = null
    @SerializedName("assign_id")
    @Expose
    var assignId: String? = null
    @SerializedName("mode_of_vehicle")
    @Expose
    var modeOfVehicle: String? = null
    @SerializedName("vehicle_id")
    @Expose
    var vehicleId: String? = null

    @SerializedName("alternate_driver_name")
    @Expose
    var alternate_driver_name: String? = null

    @SerializedName("driver_name")
    @Expose
    var driver_name: String? = null

    @SerializedName("dri_name")
    @Expose
    var dri_name: String? = null

    @SerializedName("driver_mobile_no")
    @Expose
    var driver_mobile_no: String? = null


    @SerializedName("mobile")
    @Expose
    var mobile: String? = null

    @SerializedName("alternate_driver_mob_no")
    @Expose
    var alternate_driver_mob_no: String? = null

    @SerializedName("market_vehicle")
    @Expose
    var market_vehicle: String? = null

    @SerializedName("market_alternate_driver_name")
    @Expose
    var market_alternate_driver_name: String? = null

    @SerializedName("market_alternate_driver_mob_no")
    @Expose
    var market_alternate_driver_mob_no: String? = null

    @SerializedName("dedicated_vehicle")
    @Expose
    var dedicated_vehicle: String? = null

    @SerializedName("trip_number")
    @Expose
    var trip_number: String? = null

    @SerializedName("trip_code")
    @Expose
    var trip_code: String? = null

    @SerializedName("seal_number")
    @Expose
    var seal_number: String? = null


    @SerializedName("trip_type")
    @Expose
    var trip_type: String? = null

    @SerializedName("total_scanned_boxes")
    @Expose
    var total_scanned_boxes: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("company_name")
    @Expose
    var companyName: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("customer_code")
    @Expose
    var customerCode: String? = null
    @SerializedName("contact_no")
    @Expose
    var contactNo: String? = null
    @SerializedName("service_station")
    @Expose
    var serviceStation: String? = null
    @SerializedName("status_name")
    @Expose
    var statusName: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("last_docket_details")
    @Expose
    var last_docket_details: List<PickupModel> = ArrayList()

    @SerializedName("note")
    @Expose
    var note: String? = null

    @SerializedName("step")
    @Expose
    var step: String? = null

    @SerializedName("mm_unload_step")
    @Expose
    var mm_unload_step: String? = null


    @SerializedName("lm_unload_step")
    @Expose
    var lm_unload_step: String? = null





    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("docket_no")
    @Expose
    var docket_no: String? = null

    @SerializedName("delivery_date")
    @Expose
    var delivery_date: String? = null



    @SerializedName("docket_number")
    @Expose
    var docket_number: String? = null

    @SerializedName("basic_freight")
    @Expose
    var basic_freight: String? = null

    @SerializedName("docket_freight")
    @Expose
    var docket_freight: String? = null

    @SerializedName("rov_charges")
    @Expose
    var rov_charges: String? = null

    @SerializedName("oda_charges")
    @Expose
    var oda_charges: String? = null

    @SerializedName("handling_charges")
    @Expose
    var handling_charges: String? = null

    @SerializedName("fuel_surcharge")
    @Expose
    var fuel_surcharge: String? = null

    @SerializedName("sub_total")
    @Expose
    var sub_total: String? = null

    @SerializedName("total_boxes")
    @Expose
    var total_boxes: String? = null

    @SerializedName("total_days")
    @Expose
    var total_days: String? = null

    @SerializedName("total_deps")
    @Expose
    var total_deps: String? = null

    @SerializedName("total_scanned")
    @Expose
    var total_scanned: String? = null

    @SerializedName("gst")
    @Expose
    var gst: String? = null

    @SerializedName("gst_amount")
    @Expose
    var gst_amount: String? = null

    @SerializedName("grand_total")
    @Expose
    var grand_total: String? = null


    @SerializedName("charge_perkg")
    @Expose
    var charge_perkg: String? = null


    @SerializedName("docket_charge")
    @Expose
    var docket_charge: String? = null

    @SerializedName("other_charges")
    @Expose
    var other_charges: String? = null


}
