function geocode() {
    PF('geoMap').geocode(document.getElementById('endereco').value);
    }
 
    function reverseGeocode() {
        var lat = document.getElementById('lat').value,
        lng = document.getElementById('lng').value;
 
        PF('revGeoMap').reverseGeocode(lat, lng);
}