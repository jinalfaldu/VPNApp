package com.expressvpn.securesafe.AVA_IpLocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AVA_LocationRepose
{
    @SerializedName("ip")
    @Expose
    public String ip;
    @SerializedName("network")
    @Expose
    public String network;
    @SerializedName("version")
    @Expose
    public String version;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("region")
    @Expose
    public String region;
    @SerializedName("region_code")
    @Expose
    public String regionCode;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("country_name")
    @Expose
    public String countryName;
    @SerializedName("country_code")
    @Expose
    public String countryCode;
    @SerializedName("country_code_iso3")
    @Expose
    public String countryCodeIso3;
    @SerializedName("country_capital")
    @Expose
    public String countryCapital;
    @SerializedName("country_tld")
    @Expose
    public String countryTld;
    @SerializedName("continent_code")
    @Expose
    public String continentCode;
    @SerializedName("in_eu")
    @Expose
    public Boolean inEu;
    @SerializedName("postal")
    @Expose
    public String postal;
    @SerializedName("latitude")
    @Expose
    public Float latitude;
    @SerializedName("longitude")
    @Expose
    public Float longitude;
    @SerializedName("timezone")
    @Expose
    public String timezone;
    @SerializedName("utc_offset")
    @Expose
    public String utcOffset;
    @SerializedName("country_calling_code")
    @Expose
    public String countryCallingCode;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("currency_name")
    @Expose
    public String currencyName;
    @SerializedName("languages")
    @Expose
    public String languages;
    @SerializedName("country_area")
    @Expose
    public Float countryArea;
    @SerializedName("country_population")
    @Expose
    public Integer countryPopulation;
    @SerializedName("asn")
    @Expose
    public String asn;
    @SerializedName("org")
    @Expose
    public String org;
}
