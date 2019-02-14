<template>
  <div class="home">
  	<h1>Welcome to a Microservice based Calculator implementation!</h1>
    <img alt="Vue logo" src="../assets/logo.png" style="width: 300px">
    <HelloWorld/>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'

export default {
  name: 'home',
  components: {
    HelloWorld
  }
}
</script>
