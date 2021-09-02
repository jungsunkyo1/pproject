<template>

<v-data-table
    :headers="headers"
    :items="receiptInfo"
    :items-per-page="5"
    class="elevation-1"
  ></v-data-table>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'ReceiptInfo',
    props: {
      value: Object,
      editMode: Boolean,
      isNew: Boolean
    },
    data: () => ({
        headers: [
            { text: "id", value: "id" },
            { text: "startingPoint", value: "startingPoint" },
            { text: "destinationPoint", value: "destinationPoint" },
            { text: "phoneNumber", value: "phoneNumber" },
            { text: "headcount", value: "headcount" },
            { text: "status", value: "status" },
            { text: "price", value: "price" },
            { text: "carNumber", value: "carNumber" },
        ],
        receiptInfo : [],
    }),
    async created() {
      var temp = await axios.get(axios.backend + '/receiptinfos')

      this.receiptInfo = temp.data._embedded.receiptinfos;

    },
    methods: {
    }
  }
</script>

