<template>

<v-data-table
    :headers="headers"
    :items="requestStatus"
    :items-per-page="5"
    class="elevation-1"
  ></v-data-table>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'RequestStatus',
    props: {
      value: Object,
      editMode: Boolean,
      isNew: Boolean
    },
    data: () => ({
        headers: [
            { text: "id", value: "id" },
            { text: "startingPoint", value: "startingPoint" },
            { text: "destination", value: "destination" },
            { text: "headcount", value: "headcount" },
            { text: "status", value: "status" },
            { text: "price", value: "price" },
            { text: "carNumber", value: "carNumber" },
            { text: "driverName", value: "driverName" },
        ],
        requestStatus : [],
    }),
    async created() {
      var temp = await axios.get(axios.backend + '/requeststatuses')

      this.requestStatus = temp.data._embedded.requeststatuses;

    },
    methods: {
    }
  }
</script>

