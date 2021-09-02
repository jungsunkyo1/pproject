<template>

<v-data-table
    :headers="headers"
    :items="requestAndReceiptInfo"
    :items-per-page="5"
    class="elevation-1"
  ></v-data-table>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'RequestAndReceiptInfo',
    props: {
      value: Object,
      editMode: Boolean,
      isNew: Boolean
    },
    data: () => ({
        headers: [
            { text: "id", value: "id" },
            { text: "requestId", value: "requestId" },
            { text: "recieptId", value: "recieptId" },
            { text: "startingPoint", value: "startingPoint" },
            { text: "destination", value: "destination" },
            { text: "headcount", value: "headcount" },
            { text: "phoneNumber", value: "phoneNumber" },
            { text: "carNumber", value: "carNumber" },
            { text: "driverName", value: "driverName" },
            { text: "price", value: "price" },
            { text: "status", value: "status" },
        ],
        requestAndReceiptInfo : [],
    }),
    async created() {
      var temp = await axios.get(axios.backend + '/requestandreceiptinfos')

      this.requestAndReceiptInfo = temp.data._embedded.requestandreceiptinfos;

    },
    methods: {
    }
  }
</script>

