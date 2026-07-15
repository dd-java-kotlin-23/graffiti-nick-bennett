# Graffiti web service API specifications

## General

- HTTP/HTTPS will be used for requests to the web service, and for the responses.

- Each HTTP request will include an `Authorization` header, containing an Open ID Connect/OAuth 2.0 bearer token.

- All request/response payloads will use the `application/json` MIME type.

- All UUIDs will be Base64-URL encoded, without padding and without linebreaks. 

- All timestamps will use RFC 3339 date-time format, with offsets (or `Z` for UTC), rather than time zones.

- Response status codes

    - 200 = success on `GET`, `PATCH`, `PUT`, and on `POST`of brushstroke (as described below)
    - 201 = success on `POST` (with exception of `POST` brushstroke noted below)
    - 204 = success on `DELETE` (no payload)
    - 400 = bad request, when one or more parameter values (payload properties, etc.) are invalid
    - 401 = unauthorized; no bearer token present in the request headers, or if the bearer token can't be verified.
    - 403 = client requested to delete canvas, but client isn't the owner
    - 404 = specified user or canvas not found
    - 406 = client requested `Accept` content type other than `application/json` (no response body is returned for this status)
    - 409 = conflict---e.g., user or canvas state is not consistent with a requested operation
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
        - `height`: Integer, required, must be > 0 and <= 1024
        - `width`: Integer, required, must be > 0 and <= 1024
        - `backgroundColor`: Integer (32 bits), optional, with default value `-1`
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
            - `key`: Base64 URL-encoded UUID
            - `created`: timestamp
            - `owner`: public user profile (see below)

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
            - `key`: Base64 URL-encoded UUID
            - `created`: timestamp
            - `owner`: public user profile (see below)
            - `brushstrokeCount`: integer
            - `lastBrushStroke`: timestamp

### Delete a canvas

Delete specified canvas.

- Path: `/canvases/{key}`
- Request
    - Method: `DELETE`
    - Path parameters
        - `key`: UUID, encoded using Base64 URL encoding
    - Request body ignored (`Content-type` header ignored)
- Responses
    - Unsuccessful
        - Canvas not found
        - Unauthorized
        - Forbidden (requestor is not owner)
    - Successful
        - Canvas deleted (no response body; `Accept` header ignored)

### Get a single canvas

Retrieve a single canvas, with its brushstrokes.

- Path: `/canvases/{key}`
- Request
    - Method: `GET`
    - Path parameters
        - `key`: UUID, encoded using Base64 URL encoding
- Responses
    - Unsuccessful
        - Canvas not found
        - Unauthorized
    - Successful
        - Canvas returned with brushstrokes
        - Response body:
            - `height`: Integer
            - `width`: Integer
            - `backgroundColor`: Integer
            - `created`: timestamp
            - `key`: Base64 URL-encoded UUID
            - `owner`: public user profile (see below)
            - `brushstrokeCount`: integer
            - `lastBrushStroke`: timestamp
            - `brushstrokes`: Array of
                - `added`: timestamp
                - `contributor`: public user profile
                - `color`: 32-bit integer
                - `width`: integer
                - `points`: Array of
                    - `x`: float
                    - `y`: float

### Add a brushstroke to a canvas

Add a brushstroke, specified in the request body, to a canvas specified in the URL.

- Path: `/canvases/{key}/strokes`
- Request
    - Method: `POST`
    - Path parameters
        - `key`: UUID, encoded using Base64 URL encoding
    - Request body
      - `color`: 32-bit integer, required
      - `width`: integer, required, must be between 1 and 16 (inclusive)
      - `points`: required, array (ordered) of
          - `x`: float, required, must be in [0, 1024) 
          - `y`: float, required, must be in [0, 1024)
          
          At least two points must be included; points are assumed to be in drawing order; no explicit maximum on number of points; coordinates are with respect to the canvas itself.

- Responses
    - Unsuccessful
        - Invalid property values in request payload
        - Bad `Content-type` in request
        - Bad `Accept` content type in request
        - Canvas not found
        - Unauthorized
    - Successful
        - Added brushstroke returned (no `Location` header, and status is 200)
        - Response body:
            - `added`: timestamp
            - `contributor`: public user profile
            - `color`: 32-bit integer
            - `width`: integer
            - `points`: Array of
                - `x`: float
                - `y`: float

### Get a user profile

Retrieve a single user profile.

- Path: `/users/{key}`
- Request
    - Method: `GET`
    - Path parameters
        - `key`: UUID, encoded using Base64 URL encoding
- Responses
    - Unsuccessful
        - User not found
        - Unauthorized
    - Successful
        - User profile returned
        - Response body (public user profile)
            - `key`: UUID, encoded using Base64 URL encoding
            - `displayName`: text

### Get my user profile

Retrieve the user profile of the requesting user.

- Path: `/users/me`
- Request
    - Method: `GET`
- Responses
    - Unsuccessful
        - Unauthorized
    - Successful
        - User profile returned
        - Response body (private user profile)
            - `key`: UUID, encoded using Base64 URL encoding
            - `displayName`: text
            - `created`: timestamp
            - `ownedCanvases`: integer

### Update my user profile

Replace properties of the requesting user's profile. (Currently, only the `displayName` property may be changed in this way.)

- Path: `/users/me`
- Request
    - Method: `PUT`
    - Request body:
        - `displayName`: string, length must be between 3 and 50 (inclusive)
- Responses
    - Unsuccessful
        - Unauthorized
        - Bad request, if an invalid name is specified
        - Bad `Content-type` in request
        - Bad `Accept` content type in request
        - Conflict, if specified name conflicts with another user's display name
    - Successful
        - Updated display name returned
        - Response body: private user profile 
            
