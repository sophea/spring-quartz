
var offset = "";
var limit = 10;
var isChange = false;
var hasNext = false;
var isSort = true;


$( document ).ready(function(){
	// onload
	crudLoadSchema();
	crudLoadMore();
	
	// initial date time picker
	$('.form_datetime').datetimepicker({
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		forceParse: 0,
		format: "mm/dd/yyyy, H:ii:ss P",
        showMeridian: 1
    });
	
	// delete button click
	$('#btnDelete').on('click', function(){
		var itemsToDelete = [];
		$("input.checkBox:checked").each (function () {
			itemsToDelete.push($(this).val());
		});

		if(itemsToDelete.length > 0) {
			$('#confirmModal').modal('show');
			$('#deleteAPI').on('click', function(){
				for(i in itemsToDelete) {
					crudDelete(itemsToDelete[i], function(){});
				}
				window.location.reload();
			})
		}
	});

    $("#checkAll").change(function(){
    	$(".checkBox").prop('checked', $(this).prop("checked"));
    });

    $(document).on('click', '.endLoadMore', function(){
    	$('#infoMessage').html("No more data to load");
		$('#infoModal').modal('show');
    });
    
    $('#perPage').on('change', function(){
    	limit = $(this).val();
    	offset = "";
    	$("#tableBody").empty();
    	crudLoadMore();
    })

    
    // search button click
	$("#searchBox").on("keyup", function() {
		var value = $(this).val().toLowerCase();
		$("#sortTable tr").filter(function() {
			$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		});
	});
    
    $('#btnSuccess').on('click', function(){
    	window.location.reload();
    });
    
    // add input to collection box
    $(document).on('click','.btnAddCollection',function(e){
    	key = $(this).val();
    	prefix = $(this).attr('prefix');
    	$('#' + prefix + key).append("<div class='col-xs-11'><input type='text' class='form-control margin-bottom-5 "+prefix+"collectionItem_" + key + "' placeholder='collection item'/>" +
    			"<button class='btn btn-default btnRemoveItem'><span class='glyphicon glyphicon-minus'></span></button></div>");
    	e.preventDefault();
    })
    
    // remove collection item
    $(document).on('click', '.btnRemoveItem', function(e){
    	var firstItem = $(this).prev().attr('class');
    	if(firstItem.indexOf("first") != -1) {
    		$(this).prev().val('');
    	}else{
    		$(this).parents('div.col-xs-11').remove();
    	}
    	e.preventDefault();
    })

})

/**
 * API for getting schema
 * Method: GET
 * */
function crudLoadSchema() {
    $.ajax(apiUrl + "v1/schema", {
        async: false
    })
    .success(function(data, statusText, jqXHR) {
        $("#create-entity").data("schema", data);
        $("title").text(data.tableName);
        $("#tableName").text(data.tableName);

        // All
        $("#tableHead").empty();
        $("#tableHead").append("<td><input type='checkbox' id='checkAll'></td>");
        $("#tableHead").append("<th>" + data.primaryKeyName + "</th>");

        
        $.each(data.columns, function (index, field) {

        	var key = field.name;
        	var item = field.type;

            if (!crudIsAuditField(key)) {
                $("#tableHead").append("<th>" + key + "</th>");
            }


        });
        
    })
}

function crudLoadMore() {
    if(offset == "") {
    	$("#tableBody").empty();
    }

    var body = {};
    var schema = $("#create-entity").data("schema");
    crudGetPage(body, function(data, statusText, jqXHR) {
    	hasNext = data.hasMore;
    	if(hasNext) {
    		offset = data.offset;
    		$('#btnLoadMore').attr("class","btn btn-primary");
    		$('#btnLoadMore').html("Load more");
    		$('#btnLoadMore').attr('onclick','crudLoadMore();');
    	}else{
    		$('#btnLoadMore').attr('class','btn btn-default endLoadMore');
    		$('#btnLoadMore').attr('onclick','');
    		$('#btnLoadMore').html('End of data');
    	}

        $.map(data.items, function(item, index) {
            crudAddEntity(item, index, schema);
        });
        
        if(isSort) {
        	$("#sortTable").tablesorter();
        	isSort = false;
        }else{
        	$("#sortTable").trigger("update");
        }
    });
}

function populateSelectValue(value, list) {
	$.each(list, function (index, opt) {
		if (value == opt.id) {
			value = opt.label;
			return;
			
		}
	});
	return value;
}
// add all data to table row
function crudAddEntity(item, index, schema) {
    var primaryKey = item[schema.primaryKeyName];
    $("#tableBody").append("<tr id='all_" + primaryKey + "' ><td>" +
        "<input type='checkbox' id='" + 
        primaryKey + "." + schema.primaryKeyName + "' class='checkBox' value='" + primaryKey + "'/>" +
        "</td><td><a href='#update-entity' onclick='crudPopulateUpdate(\"" + 
        primaryKey + "\");' data-toggle='tab'>"+primaryKey+"</a></td></tr>");
    var value;
    //var title = "";
    //var first = true;
    
    $.each(schema.columns, function(index, field) {
    	var key = field.name;
    	var clazz = field.type;
    	var labelValue = null;
        value = item[key];
        if(value==null) {
        	value = "";
        }
        if ("datetime" == clazz) {
            value = crudFormatMillis(value);
        } else if("select" == clazz) {
        	labelValue = populateSelectValue (value, field.data)
        }
        if (crudIsAuditField(key)) {
            //title = title + key + " " + value + ", ";
            $("#all_" + primaryKey).append("<input type='hidden' id='" + primaryKey + "X" + key + "' value='" + value + "' />");
        }
        else {
        	if (labelValue != null) {
        		$("#all_" + primaryKey).append("<td id='" + primaryKey + "Xlabel" + key + "'>" + labelValue + "</td>");
        		$("#all_" + primaryKey).append("<input type='hidden' id='" + primaryKey + "X" + key + "' value='" + value + "' />");
        	} else {
        		$("#all_" + primaryKey).append("<td id='" + primaryKey + "X" + key + "'>" + value + "</td>");
        	}
        }
    });
}

// populate update data to form
function crudPopulateUpdate(primaryKey) {
	
	 var schema = $("#create-entity").data("schema");
	 var value = primaryKey;
	    
	$('#tab-update').addClass('active');
	$('#tab-home').removeClass('active');
	hideNoResultAlert();
	
	
   
    $("#update_" + schema.primaryKeyName).val(value);
    console.log(schema.columns);
    
    $.each(schema.columns, function(index, field) {
    	
    	var clazz = field.type;
    	var key = field.name;
    	
    	var first = true;
    	if (crudIsAuditField(key) || clazz == 'select') {
            value = $("#" + primaryKey + "X" + key).val();
        }
        else {
            value = $("#" + primaryKey + "X" + key).text();
        }

        if ("text" == clazz) {
            $("#update_" + key).text(value);
        }
        else if("Collection" == clazz) {
        	$('.update_collectionBox_'+key).html('');
        	collection = value.split(",");
        	for( item in collection ){
        		var button = "";
            	var firstClass = "";
        		if(first) {
            		button = "<div class='col-xs-1'><button class='btn btn-default btnAddCollection' prefix='update_' value='" + key + "'><span class='glyphicon glyphicon-plus'></span></button></div>";
            		firstClass = "first";
        		}
            	
        		$('.update_collectionBox_'+key).append("<div class='col-xs-11'><input type='text' class='form-control "+firstClass+" margin-bottom-5 update_collectionItem_" + key + "' value='" + collection[item] + "' placeholder='collection item'/>" +
        				"<button class='btn btn-default btnRemoveItem'><span class='glyphicon glyphicon-minus'></span></button></div>"+button);
        		first = false;
        	}
        }
        else {
            $("#update_" + key).val(value);
        }
        
        //createdDate or updatedDate set readonly
        if (key == 'createdDate' || key == 'updatedDate') {
        	 $("#update_" + key).attr('readonly', true);
        }
    });
}

// format date time to [MM/DD/YYYY, H:mm:ss P]
function crudFormatMillis(millis) {
    var d = new Date(millis);
    //    return d.toUTCString();
    return d.toLocaleString();
}

// fields for skip
function crudIsAuditField(key) {
	//"createdDate" == key || "updatedDate" == key
   return "createdBy" == key ||  "updatedBy" == key  ||  "version" == key || "mainLocation" == key; 
}

// invoke update API
function crudUpdateEntity() {
    var body = crudUpsertEntity("#update_");
    delete body.createdDate;
    delete body.updatedDate;
    crudUpdate(body.id, body, function(data, statusText, jqXHR) {
    	$('#successText').html("Data update successfully.")
    	$('#successModal').modal('show');
    });
}

// collect data by id
function crudUpsertEntity(idPrefix) {
    var body = {};
    var schema = $("#create-entity").data("schema");
    var val;
    
    // add primary key?
    if ("#update_" == idPrefix || "text" == schema.primaryKeyType) {
        val = $(idPrefix + schema.primaryKeyName).val();
        if (val && 0 < val.length) {
            body[schema.primaryKeyName] = val;
        }
    }

    
    // map properties
    $.each(schema.columns, function(index, field) {
    	
    	var key = field.name;
    	var item = field.type;
    	
        if ("#create_" == idPrefix && crudIsAuditField(key)) {
            // do not map to body when creating
        }
        else {
        	if(item == "datetime") {
        		body[key] = new moment(new Date($(idPrefix + key).val())).format("YYYY-MM-DDTHH:mm:ssZZ");
        	}
        	else if(item == "Collection") {
        		body[key] = crudUpsertCollection(idPrefix, key);
        	}
        	else {
        		body[key] = $(idPrefix + key).val();
        	}
        }
    });
    return body;
}

// get all collection items
function crudUpsertCollection(idPrefix, key) {
	var collection = [];
	var prefix = "create_";
	if('#update_' == idPrefix) {
		var prefix = "update_";
	}
	$('div input.'+prefix+'collectionItem_'+key).each(function(){
		var item = $(this).val();
		if(item != "") {
			collection.push(item);
		}
	})
	return collection;
}

// populate data for search result to form
function crudPopulateReadData(data){
	var schema = $("#create-entity").data("schema");
    $("#update_" + schema.primaryKeyName).val(data[schema.primaryKeyName]);
    $.each(schema.columns, function(index, field) {
    	var key = field.name;
    	var clazz = field.type;
    	
        if (!crudIsAuditField(key)) {
        	if ("text" == clazz) {
                $("#update_" + key).text(data[key]);
            }
        	else if("datetime" == clazz) {
        		$("#update_" + key).val(crudFormatMillis(data[key]));
        	}
            else {
                $("#update_" + key).val(data[key]);
            }
        	
        }
      //createdDate or updatedDate set readonly
        if (key == 'createdDate' || key == 'updatedDate') {
        	 $("#update_" + key).attr('readonly', true);
        }
    });
}

// invoke create API
function crudCreateEntity() {
    var body = crudUpsertEntity("#create_");
    delete body.updatedDate
    delete body.createdDate
    
    crudCreate(body, function(data, statusText, jqXHR) {
    	$('#successText').html("Data create successfully.")
    	$('#successModal').modal('show');
        document.getElementById("createForm").reset();
    });
}


/**
 * API for load all with pagination
 * Method: GET
 * */
function crudGetPage(body, successFunction) {
	if(hasNext) {
		$.getJSON(apiUrl+"v1?limit="+limit+"&offset="+offset, body)
	    .success(successFunction);
    }
	else {
		$.getJSON(apiUrl+"v1?limit="+limit, body)
	    .success(successFunction);
    }
}


/**
 * API for create
 * Method: POST
 * Content-Type: application/json
 * */
function crudCreate(body, successFunction) {
	$.ajax({
		url: apiUrl + "v1/json",
		type: 'POST',
		contentType: "application/json",
		data: JSON.stringify(body),
		dataType: 'json',
		statusCode: {
			400: function(data){
				var responseObject = JSON.parse(data.responseText);
				$('#httpCode').html(responseObject.httpCode);
				$('#responseMessage').html(responseObject.message);
				$('#warningModal').modal('show');
			},
			500: function(data){
				$('#httpCode').html("500");
				$('#responseMessage').html("Something went wrong!");
				$('#warningModal').modal('show');
			}
		}
	}).success(successFunction);
}

/**
 * API for update
 * Method: POST
 * Content-Type: application/json
 * */
function crudUpdate(id, body, successFunction) {
	 delete body.updatedDate
	 delete body.createdDate
	    
	$.ajax({
		url: apiUrl + "v1/"+id+"/json",
		type: 'POST',
		contentType: "application/json",
		data: JSON.stringify(body),
		dataType: 'json',
		statusCode: {
			400: function(data){
				var responseObject = JSON.parse(data.responseText);
				$('#httpCode').html(responseObject.httpCode);
				$('#responseMessage').html(responseObject.message);
				$('#warningModal').modal('show');
			},
			500: function(data){
				$('#httpCode').html("500");
				$('#responseMessage').html("Something went wrong!");
				$('#warningModal').modal('show');
			}
		}
	}).success(successFunction);
}


/**
 * API for delete
 * Method: DELETE
 * path_variable: id
 * */
function crudDelete(id, successFunction) {
    $.ajax({
		url: apiUrl + "v1/"+id,
		type: 'DELETE',
		dataType: 'json'
	}).success(successFunction);
}

/**
 * API for read / search
 * Method: GET
 * path_variable: id
 * */
function crudRead(id, successFunction) {
    $.ajax({
		url: apiUrl + "v1/"+id,
		type: 'GET',
		dataType: 'json',
		statusCode: {
			400 : showNoResultAlert(),
			404 : showNoResultAlert()
		}
	}).success(successFunction);
}
function showNoResultAlert() {
	$('#noResult').removeClass('hide');
	$('#updateForm, #updateButton').addClass('hide');
}
function hideNoResultAlert() {
	$('#noResult').addClass('hide');
	$('#updateForm, #updateButton').removeClass('hide');
}

// allow only number
$(document).on('keypress', '.longOnly', function(event){
	var x = event.which || event.keyCode;
	if((x<48 || x>57) && x!=8 && x!=37 && x!=39){
		return false;		
	}
});

// allow decimal number
$(document).on('keypress', '.numberOnly', function(event){
	var x = event.which || event.keyCode;
	if((x<48 || x>57) && x!=8 && x!=37 && x!=39 && x!=46){
		return false;
	}
	if(x==46){
		var value = $(this).val();
		if(value.indexOf(".") != -1){
			return false;
		}
	}
});

// **** Sticky scroll
function sticky_relocate() {
    var window_top = $(window).scrollTop();
    if (window_top > 235) {
        $('#sticky').addClass('stick');
    } else {
        $('#sticky').removeClass('container stick');
    }
}
$(function() {
    $(window).scroll(sticky_relocate);
    sticky_relocate();
});


function addLeftMenu() {

    <!-- Branding Open -->
    $("#leftMenu").html(" <a class=\"brand-link navbar-primary\" href=\"index.html\">" +
		// " <img alt='Logo' class='brand-image img-circle elevation-3' style='opacity: .8' src='dist/img/AdminLTELogo.png'>" +
        "<span class=\"brand-text font-weight-light\">  Dashboard</span></a>" +
        <!-- Branding Close -->

       "<div class=\"sidebar\">" +
        <!-- Sidebar Open -->
    	"<div class=\"user-panel mt-3 pb-3 mb-3 d-flex\">" +
        "<div class=\"image\">" +
        "<img class=\"img-circle elevation-2\" alt=\"User Image\" src=\"img/user.png\">" +
        "</div>" +
        "<div class=\"info\">" +
        "<a class=\"d-block\" href=\"profile.html\">" +
        "<span>Sophea MAK</span>" +
        "</a>" +
        "</div>"+
        "</div>" +
        "<nav class=\"mt-2\">" +
        "<ul class=\"nav nav-pills nav-sidebar flex-column\" data-widget=\"treeview\" role=\"menu\" data-accordion=\"false\">"+
        "<li class=\"nav-item\"> <a class=\"nav-link\" href=\"holiday.html\"> <i class=\"far fa-circle nav-icon\"></i> <p> Holiday </p> </a> </li>" +
        "<li class=\"nav-item\"> <a class=\"nav-link\" href=\"jobs.html\"> <i class=\"far fa-circle nav-icon\"></i> <p> Jobs </p> </a> </li>" +
        "<li class=\"nav-item\"> <a class=\"nav-link\" href=\"javascript:logout();\"> <i class=\"nav-icon fas fa-sign-out-alt\"></i> <p> Logout </p> </a> </li>" +
		"</ul></nav>" +
        <!-- /.sidebar-menu -->
		"</div>"
	);
}


function storeToken(token) {
    localStorage.setItem('token', token);
}
function getToken() {
    return this.localStorage.getItem('token');
}

function logout() {
	this.localStorage.clear();
	window.location.href="index.html";
}