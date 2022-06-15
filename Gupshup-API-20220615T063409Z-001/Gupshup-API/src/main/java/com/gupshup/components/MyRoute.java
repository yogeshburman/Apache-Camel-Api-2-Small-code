package com.gupshup.components;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import org.springframework.stereotype.Component ;

@Component
public class MyRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception{
		
	restConfiguration().component("servlet").host("localhost").port(9090).bindingMode(RestBindingMode.auto);
		
		// post route
		rest().post("/setresponse")
		.route()
		.choice()
			.when(jsonpath("$.payload.type").in("text","video","image"))
			.setHeader("Quename",jsonpath("$.payload.type"))
			.toD("activemq:queue:${headers.Quename}?exchangePattern=InOnly")
		.otherwise()
			.setHeader("Quename",constant("WA-Error"))
			.toD("activemq:queue:${headers.Quename}?exchangePattern=InOnly")
		.endChoice();
		}
}
