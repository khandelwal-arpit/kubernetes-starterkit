<template>
  <div class="calculator" id="app">
    <h1>History</h1>
    <p>Calculator History</p>
    <div class="display">
      <p>
        <ul id="history">
          <li v-for="operation in history">
          {{ operation }}
          </li>
        </ul>
      </p>
    </div>
  </div>
</template>

<script>
import BootStorageService from '@/services/BootStorageService'
export default {
  name: 'history',
  data () {
		  return {
        history: ''
		  }
  },
  mounted () {
    this.getHistory();
  },
  methods: {
    async getHistory()
    {
      const response = await BootStorageService.getOperations();
      this.history = response.data;
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
// Color
$darkBlue: #2c3e50;
$green: #42b983;
$lightGreen: lighten($green,30%);
$grey: #7f8c8d;
$lightGrey: #f8f8f8;

//fonts
@import url(https://fonts.googleapis.com/css?family=Dosis|Source+Sans+Pro|Space+Mono);
$heading-font: 'Dosis', 'Source Sans Pro', 'Helvetica Neue', Arial, sans-serif;
$body-font: 'Source Sans Pro', 'Helvetica Neue', Arial, sans-serif;
$monospace: 'Space Mono', monospace;

* {
   box-sizing: border-box;
}

html, body {
   width: 100%;
}

button, button:focus {
   outline: none;
   appearance: none;
}

ul {
  text-align: left;
  max-height: 200px;
  overflow-y: auto;
}

.calculator {
   width: 350px;
   margin: 50px auto 10px auto;
   padding: 25px 10px;
   position: relative;
   font-family: $body-font;
   //border: rgba($grey,0.3) solid 2px;
   border: rgba(127, 140, 141, 0.3) solid 4px;
   border-radius: 10px;
   box-shadow: -3px 3px 7px rgba(0, 0, 0, .6), inset -100px 0px 100px rgba(255, 255, 255, .5);
   background-color: #ABABAB;
   color: $green;
   text-align: center;

   p { margin: 0;}

   // Heading
   h1 {
      margin: 0;
      font-family: heading-font;
      font-size: 2.5em;
      &+ p {
         margin: 0px;
         font-size: 1.2em;
         color: $grey;
      }
   }
   .display {
      background-color: $lightGrey;
      margin: 10px auto;
      padding: 10px;
      border: solid 0.5px rgba($grey, 0.4);
      border-radius: 10px;
      box-shadow: inset 0px 0px 10px #030303, inset 0px 0px 1px rgba(150, 150, 150, .2);
   }
   input[type="text"] {
      width: 100%;
      margin: 0;
      padding: 5px 10px;
      border: none;
      background-color: transparent;
      font-family: $monospace;
      text-align: right;
      &#result {
         font-size: 1.8em;
         font-weight: bolder;
         border-bottom: rgba(0,0,0,0.1) solid 0.5px;
      }
      &#calculation {
         font-size: 1.3em;
      }

   }

   .row {
      display: flex;
      position: relative;
      width: 100%;
      flex-flow: row nowrap;

      button {
         width: 100%;
         height: 60px;
         margin: 5px;
         background-color: #EBEBEB;
         color: #181818;
         border: inside $lightGreen solid 1.4px;
         border-radius: 10px;
         box-shadow: 0px 0px 2px #030303, inset 0px 0px 1px #DCDCDC;
         font-size: 1.3em;
         font-weight: bolder;
         transition: all 0.4s, background-color 0.1s ease-in;

         &:hover {
            background-color: rgba($green,0.8);
         }

         &:active {
            transform: scale(0.8,0.8);
            border: none;
         }
      }

      .tall {
         min-height: 125px;
      }

      &:last-child {
         display: flex;
         position: absolute;
         bottom: calc(100% - 580px);
         width: 71%;
      }
   }
}
</style>
