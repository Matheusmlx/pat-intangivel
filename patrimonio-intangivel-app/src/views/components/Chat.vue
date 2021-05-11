<template>
    <div></div>
</template>

<script>
    export default {
        name: 'Chat',
        props: {
            url: {
                required: true
            }
        },
        created() {
            this.start()
        },
        methods: {
            start() {
                let s1=document.createElement('script'),s0=document.getElementsByTagName('script')[0]
                s1.async=true
                s1.src=this.url
                s1.charset='UTF-8'
                s1.setAttribute('crossorigin','*')
                s0.parentNode.insertBefore(s1,s0)
            }
        }
    }
</script>


