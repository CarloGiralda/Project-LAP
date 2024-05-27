const apiGw = 'http://localhost:8080'

// list all endpoints here
const API_ENDPOINTS = {
    createPayment: apiGw + '/payment/create',
    authJwtToken: apiGw + '/auth/token',
    extractUsernameJwtToken: apiGw + '/auth/extractUsernameFromJwt'
}
