			var currentMarker = null;
		 
		    function handlePointClick(event) {
		        if(currentMarker === null) {
		            document.getElementById('lat').value = event.latLng.lat();
		            document.getElementById('lng').value = event.latLng.lng();
		 
		            currentMarker = new google.maps.Marker({
		                position:new google.maps.LatLng(event.latLng.lat(), event.latLng.lng())
		            });
		 
		            PF('geoMap').addOverlay(currentMarker);
		 
		            PF('dialogMarkerSelected').show();
		        }   
		    }
		 
		    function markerAddComplete() {
		        var nome = document.getElementById('nome');
		        var telefone = document.getElementById('telefone');
		        var detalhes = document.getElementById('detalhes');
		        var obs = document.getElementById('obs');
		        
		        if((nome.value = "") || (telefone.value == "") || (detalhes.value == ""))
		        	return;
		        
		        currentMarker.setTitle(nome.value);
		        nome.value = "";
		        telefone.value = "";
		        detalhes.value = "";
		        obs.value = "";
		 
		        currentMarker = null;
		        PF('dialogMarkerSelected').hide();
		    }
		 
		    function cancel() {
		        PF('dialogMarkerSelected').hide();
		        currentMarker.setMap(null);
		        currentMarker = null;
		 
		        return false;
		    }