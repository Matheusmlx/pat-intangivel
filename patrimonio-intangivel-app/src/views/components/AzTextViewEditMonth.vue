<template>
<div>
    <v-dialog
        ref="dialog"
        v-model="model"
        width="290px">
        <template v-slot:activator="{ on }">
            <v-text-field
                v-model="dateFormatted"
                :name="name"
                :outlined="outlined"
                :dense="dense"
                :error-messages="errors.collect(name)"
                v-validate="validate"
                append-icon="event"
                readonly
                :min-date="minDate"
                :max-date="maxDate"
                :v-mask="dateMask"
                :placeholder="placeholderDate"
                v-on="on"
                @click:append="openMenuDate">
                <template v-slot:label>
                    <slot name="label" />
                </template>
            </v-text-field>
        </template>
        <v-date-picker
            v-model="date"
            type="month"
            :min="minDate"
            :max="maxDate"
            @change="submitEdit"
            scrollable>
            <v-spacer/>
            <v-btn text color="primary" @click="model = false">Cancelar</v-btn>
        </v-date-picker>
    </v-dialog>
</div>
</template>

<script>
    export default {
        name: 'AzTextViewEditMonth',
        data() {
            return {
                date: undefined,
                model: false,
                dateFormatted: null
            }
        },
        props: {
            outlined:{
                type:Boolean,
                default:false
            },
            dense:{
                type:Boolean,
                default:false
            },
            minDate: {
                type: String
            },
            maxDate: {
                type: String
            },
            placeholderDate: {
                type: String,
                default: 'MM/YYYY'
            },
            dateMask: {
                type: String,
                default: '##/####'
            },
            name: {
                required: true,
                type: String
            },
            validate: {
                type: String,
            },
        },
        watch: {
            date(val) {
                this.dateFormatted = this.formatDate(this.date)
            },
        },

        methods: {
            formatDate(date) {
                if (!date) return null
                const [year, month] = date.split('-')
                return `${month}/${year}`
            },
            async submitEdit() {
                this.model = false
                this.$emit('input', this.date)
            },
            openMenuDate() {
                this.model = true
            }
        }
    }
</script>

<style scoped>

</style>

