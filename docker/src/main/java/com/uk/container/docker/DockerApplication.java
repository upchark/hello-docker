package com.uk.container.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DockerApplication {

	static Class<?> appClass = DockerApplication.class;

	public static void main(String[] args) {
		SpringApplication.run(appClass, args);
	}

	/**
	 * @param id:@PathVariable::Annotation   which indicates that a method parameter
	 *                                       should be bound to a URI
	 *                                       template variable. Supported for
	 *                                       RequestMapping annotated handler
	 *                                       methods. If the method parameter is
	 *                                       Map<String, String>then the map is
	 *                                       populated with all path variable names
	 *                                       and values.
	 * 
	 * @param name:@RequestParam::Annotation which indicates that a method parameter
	 *                                       should be bound to a web request
	 *                                       parameter.Supported for annotated
	 *                                       handler methods in Spring MVC and
	 *                                       Spring WebFluxas follows: •In Spring
	 *                                       MVC, "request parameters" map to query
	 *                                       parameters, form data,and parts in
	 *                                       multipart requests. This is because the
	 *                                       Servlet API combines query parameters
	 *                                       and form data into a single map called
	 *                                       "parameters", and that includes
	 *                                       automatic parsing of the request body.
	 *                                       •In Spring WebFlux, "request
	 *                                       parameters" map to query parameters
	 *                                       only.To work with all 3, query, form
	 *                                       data, and multipart data, you can use
	 *                                       data binding to a command object
	 *                                       annotated with ModelAttribute.If the
	 *                                       method parameter type is Map and a
	 *                                       request parameter name is specified,
	 *                                       then the request parameter value is
	 *                                       converted to a Map assuming an
	 *                                       appropriate conversion strategy is
	 *                                       available.If the method parameter is
	 *                                       Map<String, String> or
	 *                                       MultiValueMap<String, String>and a
	 *                                       parameter name is not specified, then
	 *                                       the map parameter is populated with all
	 *                                       request parameter names and values.
	 * 
	 * @return
	 */
	@GetMapping("docker/access/did/{did}")
	public String getAccess(@PathVariable(name = "did") String id
			, @RequestParam(name = "name") String name
			)
	{
		if (id.contains("docker")) {
			return "Access Granted:"+name;
		}
		return "Access Denied:"+name;
	}

}
