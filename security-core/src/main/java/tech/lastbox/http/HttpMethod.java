package tech.lastbox.http;

public enum HttpMethod {
    GET("Retrieve data"),
    POST("Submit data"),
    PUT("Update or replace data"),
    DELETE("Remove data"),
    PATCH("Partially update data"),
    HEAD("Retrieve headers only"),
    OPTIONS("Describe communication options"),
    TRACE("Echo request for debugging"),
    CONNECT("Establish a tunnel");

    private final String description;

    HttpMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
