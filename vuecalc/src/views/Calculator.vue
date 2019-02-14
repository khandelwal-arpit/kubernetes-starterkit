<template>
  <div class="calculator" id="app">
    <h1>Calculator</h1>
    <p>Created By - Arpit Khandelwal</p>
    <div class="display">
      <p>
        <input type="text" v-model="result" disabled="disabled" id="result"/>
      </p>
      <p>
        <input type="text" v-model="calculation" disabled="disabled" id="calculation"/>
      </p>
    </div>
    <p class="row">
      <button v-on:click="buttonclicked('AC')">AC</button>
      <button v-on:click="buttonclicked('CE')">CE</button>
      <button v-on:click="buttonclicked('/')">/</button>
      <button v-on:click="buttonclicked('x')">x</button>
    </p>
    <p class="row">
      <button v-on:click="buttonclicked('7')">7</button>
      <button v-on:click="buttonclicked('8')">8</button>
      <button v-on:click="buttonclicked('9')">9</button>
      <button v-on:click="buttonclicked('-')">-</button>
    </p>
    <p class="row">
      <button v-on:click="buttonclicked('4')">4</button>
      <button v-on:click="buttonclicked('5')">5</button>
      <button v-on:click="buttonclicked('6')">6</button>
      <button v-on:click="buttonclicked('+')">+</button>
    </p>
    <p class="row">
      <button v-on:click="buttonclicked('1')">1</button>
      <button v-on:click="buttonclicked('2')">2</button>
      <button v-on:click="buttonclicked('3')">3</button>
      <button v-on:click="buttonclicked('=')" class="tall">=</button>
    </p>
    <p class="row">
      <button v-on:click="buttonclicked('0')">0</button>
      <button v-on:click="buttonclicked('.')">.</button>
    </p>
  </div>
</template>

<script>
import ExpressedService from '@/services/ExpressedService'
import HappyService from '@/services/HappyService'

export default {
  name: 'calculator',
  data () {
		  return {
      memory: '',
      current: '',
      result: '0'
		  }
  },
  computed: {
    calculation: {
      get () {
        if (this.current && this.current.length > 0) {
				   		return this.memory + this.current.match(/(\d+[.]\d+)|\d+/g).join('');
	      }
	      return this.memory;
      }
    }
  },
  methods: {
	      calculate: async function (num1, num2, opr, callback) {
    			num1 = parseFloat(num1);
    			num2 = parseFloat(num2);
    			switch (opr) {
    				case '+':
              try{
                  console.log('Hitting web api for addition')
                  const response = await ExpressedService.getAdditionResult(num1, num2);
                  console.log('Returned result=' + response.data.result);
                  callback(response.data.result);
              } catch(e){
                  callback(e);
              }
    					break;
    				
            case '-':
              try{
                  console.log('Hitting web api for subtraction')
                  const response = await ExpressedService.getSubtractionResult(num1, num2);
                  console.log('Returned result=' + response.data.result);
                  callback(response.data.result);
              } catch(e){
                  callback(e);
              }
              break;
    				
    				case 'x':
              try{
                  console.log('Hitting web api for multiply')
                  const response = await HappyService.getMultiplicationResult(num1, num2);
                  console.log('Returned result=' + response.data);
                  callback(response.data);
              } catch(e){
                  callback(e);
              }
              break;
 
    				case '/':
    					if (num2 === 0) {
                  callback(0);
              } else {
                  try{
                    console.log('Hitting web api for division')
                    const response = await HappyService.getDivisionResult(num1, num2);
                    console.log('Returned result=' + response.data);
                    callback(response.data);
                  } catch(e){
                    callback(e);
                  }
              }
    					break;

    				default:
    					console.log('default')
    					callback(num1 + num2)
    				}
		    },
		    buttonclicked: function (val) {
  		   	var self = this;
  				var bVal = val;

  				// When AC button has been pressed
  				if (bVal == 'AC') {
  					this.memory = ''
  					this.current = ''
  					this.result = 0
  				} else

  				// When CE button has been pressed
  				if (bVal == 'CE') {
  					this.current = ''
  				} else

  				// When number buttons have been pressed
  				if (!isNaN(bVal)) {
  					this.current += bVal
  				} else

  				// When dot button has been pressed
  				if (bVal == '.') {
  					if (!this.current) { this.current += 0 }
  					this.current += '.'
  				} else

  				if (bVal == '=') {
  					var firstNumb = (this.calculation.split(''))[0];
  					var numbers = this.calculation.match(/(\d+[.]\d+)|\d+/g);
            var operation = this.calculation.match(/[+]|[-]|[x]|[/]/g);
            var result;

  					if (firstNumb >= 0) {
  						operation = ['+'].concat(operation)
  					} else
  					if (firstNumb === '-') {
  						numbers[0] = -numbers[0]
  					}

  					result = numbers.reduce(function (prev, curr, idx) {
  						console.log(numbers)
  						console.log(firstNumb)
  						console.log(operation[idx])
  						return self.calculate(prev, curr, operation[idx], function (apiresult) {
                console.log('result:' + apiresult)
                self.result = apiresult
                self.memory = apiresult
                self.current = ''
              })
  					});

  				} else {
    					if (!this.current) {
    						this.memory += bVal;
    					} else {
    						this.memory += this.current + bVal;
    					}
    					this.current = '';
  				}
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
