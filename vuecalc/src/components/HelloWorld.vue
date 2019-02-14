<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <div class="description">
        The purpose of this implementation was to create a web application that has the following components -
        <ol>
          <li>A Vue.js based frontend</li>
          <li>A microservice to do add and subtract operations</li>
          <li>A microservice to do multiplication and division operations</li>
          <li>A microservice to store the history of all caculator operations in Redis</li>
          <li>Containerised servers</li>
          <li>Kubernetes cluster basd deployment</li>
        </ol>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.description {
  text-align: left; 
  font-size: 20px; 
  font-weight:600; 
  margin-left: 125px;
}
a {
  color: #42b983;
}
</style>
