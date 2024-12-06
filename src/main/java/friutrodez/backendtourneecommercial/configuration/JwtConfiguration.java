package friutrodez.backendtourneecommercial.configuration;

public class JwtConfiguration {
    // TODO : fait un flow chart pour voir le process que doit faire spring avec
    //  l'authentification avec un token jwt
    // /\
    // /\
    // /\
    // On authentification -> connect with username and password -> verify password and existence of username
    // if(exist) then
    //  give token (create token) and connect user and connect user in spring and lauch good request to front end
    // else
    // do not connect and lauch bad request to front end with details

    // On Create -> ask for username and password -> verify non existence of username and difficulty of password
    // if(good) then
    // give good request to front end and create user in mysql and specify role for user and ask user for connection
    // else
    // give bad request with details

    // for user access when connected
    // is token provided
    // no then : do not accept request
    // yes then : verify token with connected user exist
    // if(exits
    // accept request and execute
    // else
    // do not accept and send front end response with details


}
