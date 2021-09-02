
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import TaxiRequestManager from "./components/TaxiRequestManager"

import RequestStatus from "./components/RequestStatus"
import RecieptManager from "./components/RecieptManager"

import ReceiptInfo from "./components/ReceiptInfo"
import PaymentManager from "./components/PaymentManager"


import RequestAndReceiptInfo from "./components/RequestAndReceiptInfo"
export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/taxiRequests',
                name: 'TaxiRequestManager',
                component: TaxiRequestManager
            },

            {
                path: '/requestStatuses',
                name: 'RequestStatus',
                component: RequestStatus
            },
            {
                path: '/reciepts',
                name: 'RecieptManager',
                component: RecieptManager
            },

            {
                path: '/receiptInfos',
                name: 'ReceiptInfo',
                component: ReceiptInfo
            },
            {
                path: '/payments',
                name: 'PaymentManager',
                component: PaymentManager
            },


            {
                path: '/requestAndReceiptInfos',
                name: 'RequestAndReceiptInfo',
                component: RequestAndReceiptInfo
            },


    ]
})
