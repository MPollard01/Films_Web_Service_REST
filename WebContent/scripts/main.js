$(document).ready(function(){
	$.ajax({ method: 'GET',
		url: 'rest/films',
        dataType: 'json',
        success: function(response){
        	console.log(response);
        	var head = headers();
        	$('#films').append(head);
            jsonTable(response);
            deleteInit();
        }});
        
});

$('#format').change(function(){
	var format = $('#format').val();
	var url;
	if(format === 'json' || format === 'xml') url = 'rest/films';
	else url = 'rest/films/text';
	$.ajax({ method: 'GET',
		url: url,
        dataType: format,
        success: function(response){
        	getFilms(response, format);
            deleteInit();
        }});
});

$('#searchfilm').submit(function(e){
	e.preventDefault();
	var format = $('#format').val();
	var option = $('input[name="optradio"]:checked').val();
	var data = $('#search').val();
	var url;
	if(format === 'json' || format === 'xml') url = 'rest/films/'+option+'/'+data;
	else url = 'rest/films/'+option+'/text/'+data;
	console.log(data);
	$.ajax({ method: 'GET',
		url: url,
		dataType: format,
        success: function(response){
        	console.log(response);
        	var head = headers();
        	$('#films').html(head);
            if(format === 'json') {
            	if(option === 'id') jsonRow(response);
            	else jsonRows(response);
            }
            else if(format === 'xml') xmlTable(response);
            else {
            	var string = response.split('#');
            	var data = stringRow(string);
				$('.table').append(data);
            }
        }});
});

$('#create').submit(function(e){
	e.preventDefault();
	var format = $('#format').val();
	var data, content, url = 'rest/films/create';
	if(format === 'json') {
		data = getJsonData('c');
		content = 'application/json';
	}
	else if(format === 'xml'){
		data = getXmlData('c');
		content = 'application/xml';
	}
	else {
		data = getTextData('c');
		content = 'text/plain';
		url = 'rest/films/create/text'
	}
	$.ajax({ method: 'POST',
		url: url,
		dataType: format,
		data: data,
		contentType: content,
        success: function(response){
            getFilms(response, format);
            
        }});
});

$('#update').submit(function(e){
	e.preventDefault();
	var format = $('#format').val();
	var data, content, url = 'rest/films/update';
	if(format === 'json') {
		data = getJsonData('u');
		content = 'application/json';
	}
	else if(format === 'xml'){
		data = getXmlData('u');
		content = 'application/xml';
	}
	else {
		data = getTextData('u');
		content = 'text/plain';
		url = 'rest/films/update/text'
	}
	$.ajax({ method: 'PUT',
		url: url,
		dataType: format,
		data: data,
		contentType: content,
        success: function(response){
        	getFilms(reponse, format);
            
        }});
});

function deleteInit() {
	$('.delete').click(function(e){
		e.preventDefault();
		var format = $('#format').val();
		var url;
		if(format === 'json' || format === 'xml') url = 'rest/films/delete/'+$(this).attr('id');
		else url = 'rest/films/delete/text/'+$(this).attr('id');
		$.ajax({ method: 'DELETE',
			url: url,
			dataType: format,
			data: $(this).attr('id'),
			contentType: 'application/json',
	        success: function(response){
	        	getFilms(response, format);
	            
	        }});
	});
}

function getFilms(response, format) {
	console.log(response);
	var head = headers();
	$('#films').html(head);
    if(format === 'json') jsonTable(response);
    else if(format === 'xml') xmlTable(response);
    else {
    	var strings = response.split(/[\n\r]+/);
		var rows = new Array(strings.length);
	    for(var i=0; i<strings.length; i++) {
	      rows[i] = strings[i].split("#");
	    }
		var data = stringTable(rows);
		$('.table').append(data);
    }
}

function getJsonData(form) {
	var data = {
		id: $('#'+form+'id').val(),
		title: $('#'+form+'title').val(),
		year: $('#'+form+'year').val(),
		director: $('#'+form+'director').val(),
		stars : $('#'+form+'stars').val(),
		review: $('#'+form+'review').val()
	};
	
	return JSON.stringify(data);
}

function getXmlData(form) {
	var data = '<film>'+
				'<id>'+$('#'+form+'id').val()+'</id>'+
				'<title>'+$('#'+form+'title').val()+'</title>'+
				'<year>'+$('#'+form+'year').val()+'</year>'+
				'<director>'+$('#'+form+'director').val()+'</director>'+
				'<stars>'+$('#'+form+'stars').val()+'</stars>'+
				'<review>'+$('#'+form+'review').val()+'</review>'+
			   '</film>';
	
	return data;
}

function getTextData(form) {
	var data =
	$('#'+form+'id').val()+'#'+
	$('#'+form+'title').val()+'#'+
	$('#'+form+'year').val()+'#'+
	$('#'+form+'director').val()+'#'+
	$('#'+form+'stars').val()+'#'+
	$('#'+form+'review').val();
	return data;
}

function headers() {
	var output = 
			'<table class="table">' +
			'<tr class="thead-light">' +
			'<th><b>ID</b></th>' +
			'<th><b>Title</b></th>' +
			'<th><b>Year</b></th>' +
			'<th><b>Director</b></th>' +
			'<th><b>Stars</b></th>' +
			'<th><b>Review</b></th>' +
			'<th>delete</th>' +
		 	'</tr>';
	
	return output;
}

function jsonTable(json) {
	$.map(json, function(films, i){
	            $.each(films, function(i, film){
	            	$('.table').append(
			            '<tr>' +
						'<td>'+films[i].id+'</td>' +
						'<td>'+films[i].title+'</td>' +
						'<td>'+films[i].year+'</td>' +
						'<td>'+films[i].director+'</td>' +
						'<td>'+films[i].stars+'</td>' +
						'<td>'+films[i].review+'</td>' +
						'<td><form class="delete" id="'+films[i].id+'"><button type="button" class="btn btn-sm btn-danger">Delete</button></form></td>' +
						'</tr></table>'
	            	);
	            });
            });	
       
}

function jsonRow(films) {
	$('.table').append(
            '<tr>' +
			'<td>'+films.id+'</td>' +
			'<td>'+films.title+'</td>' +
			'<td>'+films.year+'</td>' +
			'<td>'+films.director+'</td>' +
			'<td>'+films.stars+'</td>' +
			'<td>'+films.review+'</td>' +
			'<td><form class="delete" id="'+films.id+'"><button type="button" class="btn btn-sm btn-danger">Delete</button></form></td>' +
			'</tr></table>'
    	);
}

function jsonRows(films) {
	$('.table').append(
            '<tr>' +
			'<td>'+films.film.id+'</td>' +
			'<td>'+films.film.title+'</td>' +
			'<td>'+films.film.year+'</td>' +
			'<td>'+films.film.director+'</td>' +
			'<td>'+films.film.stars+'</td>' +
			'<td>'+films.film.review+'</td>' +
			'<td><form class="delete" id="'+films.film.id+'"><button type="button" class="btn btn-sm btn-danger">Delete</button></form></td>' +
			'</tr></table>'
    	);
}

function xmlTable(xml) {
	 $("film", xml).each(function () {
              var id = $(this).find("id").text();
              var title = $(this).find("title").text();
              var year = $(this).find("year").text();
              var director = $(this).find("director").text();
              var stars = $(this).find("stars").text();
              var review = $(this).find("review").text();
              $('.table').append(
                      '<tr>' +
						'<td>'+id+'</td>' +
						'<td>'+title+'</td>' +
						'<td>'+year+'</td>' +
						'<td>'+director+'</td>' +
						'<td>'+stars+'</td>' +
						'<td>'+review+'</td>' +
						'<td><form class="delete" id="'+id+'"><button type="button" class="btn btn-sm btn-danger">Delete</button></form></td>' +
						'</tr></table>'
              );
          });
}

function stringTable(rows) {
  var body = '';
  for(var i=0; i<rows.length; i++) {
    body += '<tr>';
    var row = rows[i];
    for(var j=0; j<row.length; j++) {
      body += '<td>' + row[j] + '</td>';
    }
    body += 
    	'<td><form class="delete" id="'+row[0]+'"><button type="button" class="btn btn-sm btn-danger">Delete</button></form></td>' +
    	'</tr>';
  }
  body += '</table>';
  return(body);
}

function stringRow(string) {
	$('.table').append(
              '<tr>' +
				'<td>'+string[0]+'</td>' +
				'<td>'+string[1]+'</td>' +
				'<td>'+string[2]+'</td>' +
				'<td>'+string[3]+'</td>' +
				'<td>'+string[4]+'</td>' +
				'<td>'+string[5]+'</td>' +
				'<td><form class="delete" id="'+string[0]+'"><button type="button" class="btn btn-sm btn-danger">Delete</button></form></td>' +
				'</tr></table>'
      );
}