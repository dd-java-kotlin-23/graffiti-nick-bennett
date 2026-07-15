# Graffiti web service API specifications

## General

- HTTP/HTTPS will be used for requests to the web service, and for the responses.

- Each HTTP request will include an `Authorization` header, containing an Open ID Connect/OAuth 2.0 bearer token.

- All request/response payloads will use the `application/json` MIME type.

- Response status codes

    - 200 = success on `GET`, `PATCH`, `PUT`, and some `POST` (as described below)
    - 201 = success on `POST` (with some exceptions noted below)
    - 204 = success on `DELETE` (no payload)
    - 400 = bad request, when one or more parameter values (payload properties, etc.) are invalid
    - 401 = unauthorized; no bearer token present in the request headers, or if the bearer token can't be verified.
    - 403 = client requested to delete canvas, but client isn't the owner
    - 404 = specified user or canvas not found
    - 406 = client requested `Accept` content type other than `application/json`
    - 409 = user or canvas state is not consistent with a requested operation
    - 415 = client sent paylod using `Content-type` other than `application/json`

- Error payloads

    For all but the 406 error, the response will include the following in the payload:

    - timestamp
    - status (same as HTTP status)
    - error (canonical error for status)
    - message (optional: more detailed description of error)
    - path (URL of request)

## Requests

### Create a canvas

Defines a new canvas, using the authenticated user as the owner, and with specified dimensions and background color.

- Path: `/canvases`
- Request
    - Method: `POST`
    - Request body
        - `height`: Integer, must be > 0 and <= 1024
        - `width`: Integer, must be > 0 and <= 1024
        - `backgroundColor`: Integer (32 bits), with default value `-1`
- Responses
    - Unsuccessful 
        - Invalid `height` or `width` values
        - Unauthorized
        - Bad content type in request payload
        - Bad `Accept` content type
    - Successful
        - Canvas created
        - Headers
            - `Location` of created resource
        - Response body
            - `height`: Integer
            - `width`: Integer
            - `backgroundColor`: Integer
            - `created`: timestamp
            - `owner`: user profile (see below)

### Get a list of canvases

Retrieve a list of canvas resources, optionally within a date range.

- Path: `/canvases`
- Request
    - Method: `GET`
    - Request parameters in query string
        - `start`: timestamp (optional, default is 30 days before current datetime)
        - `end`: timestamp (optional, default is current datetime; must be after `start`)
- Responses
    - Unsuccessful
        - Invalid combination of `start` and `end`
        - Unauthorized
        - Bad `Accept` content type
    - Successful
        - Selected canvases returned 
        - Response body
            Array of:
            - `height`: Integer
            - `width`: Integer
            - `backgroundColor`: Integer
            - `created`: timestamp
            - `owner`: user profile (see below)
            - `brushstrokeCount`: integer
            - `lastBrushStroke`: timestamp



### Delete a canvas

Delete specified canvas.

- Path: `/canvases/{key}`
- Request
    - Method: `DELETE`
    - Path parameters
        - `key`: UUID, encoded using Base64 URL encoding
- Responses
    - Unsuccessful
        - Canvas not found
        - Unauthorized
        - Forbidden (requestor is not owner)
    - Successful
        - Canvas deleted

