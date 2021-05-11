<template>
    <v-snackbar v-model='show' :color='color' right top :timeout='timeout'>
        <span :style='style'>{{text}}</span>
        <a @click='undoAction' :style='styleButtom'>{{mensageButtom}}</a>
        <v-btn dark text @click='closeShow' :style="styleButtomClose">
            <v-icon small :color='iconColor'>close</v-icon>
        </v-btn>
    </v-snackbar>
</template>

<script>
    export default {
        props: {
            show: {
                default: false
            },
            text: {
                type: String,
                required: true,
                default: ''
            },
            style: {
                type: String,
                default: 'color:#907d43;font-weight:bold;margin-left:10px;'
            },
            color: {
                type: String,
                default: '#fef3cc'
            },
            mensageButtom: {
                type: String,
                default: 'Desfazer'
            },
            styleButtom: {
                type: String,
                default: 'color:#907d43;font-weight:bold; border-bottom: 2px solid #907d43;margin-left:15px;margin-right:5px'
            },
            iconColor: {
                type: String,
                default: '#907d43'
            },
            styleButtomClose: {
                type: String,
                default: 'margin-right:-20px;magin-left:30px;'
            }
        },
        name: 'AzAlert',
        data() {
            return {
                hasButtom: false,
                timeout: 6000,

            }
        },
        methods: {
            async undoAction() {
                this.$emit('desfazer', true)
            },
            closeShow() {
                this.show = false
                this.$emit('closeButtom')
            }
        }
    }
</script>
