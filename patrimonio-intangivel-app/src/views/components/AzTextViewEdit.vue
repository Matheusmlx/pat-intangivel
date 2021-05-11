<template>
    <div class="az-text az-text-edit">
        <div v-if="!editing">
            <label>{{ label }}</label>
            <v-card class="elevation-0">
                <v-card-text class="pa-0">
                    <p class="d-inline">{{ value? value : '-' }}
                        <v-tooltip top v-if="!disabled">
                            <template v-slot:activator="{ on }">
                                <v-btn v-on="on" icon class="start-edit-btn">
                                    <v-icon class="mt-1" x-small @click="startEdit">fas fa-edit</v-icon>
                                </v-btn>
                            </template>
                            {{labelBtnEdit}}
                        </v-tooltip>
                    </p>
                </v-card-text>
            </v-card>
        </div>

        <v-text-field
            v-if="editing && (!mask || mask === '')"
            v-model="model"
            :label="label"
            :name="name"
            :maxlength="maxlength"
            :error-messages="errors.collect(name)"
            :counter="counter"
            :placeholder="placeholder"
            v-validate="validate"
            :data-vv-as="name"
            :estaAdicionando="estaAdicionando"
            @keyup.enter="submitEdit"
            @keyup.esc="cancelEdit"
            @click:append="cancelEdit"
            @click:clear="submitEdit">
            <template v-slot:append>
                <v-tooltip top>
                    <template v-slot:activator="{ on }">
                        <v-btn v-on="on" icon>
                            <v-icon small class="primary--text" @click="submitEdit">fas fa-check</v-icon>
                        </v-btn>
                    </template>
                    {{labelBtnSave}}
                </v-tooltip>
                <v-tooltip top>
                    <template v-slot:activator="{ on }">
                        <v-btn v-on="on" icon>
                            <v-icon small @click="cancelEdit">fas fa-times</v-icon>
                        </v-btn>
                    </template>
                    {{labelBtnCancel}}
                </v-tooltip>
            </template>
        </v-text-field>

        <v-text-field
            v-if="editing && mask"
            v-model="model"
            :label="label"
            :name="name"
            :maxlength="maxlength"
            :error-messages="errors.collect(name)"
            :counter="counter"
            :placeholder="placeholder"
            v-validate="validate"
            :data-vv-as="name"
            v-mask="mask"
            :estaAdicionando="estaAdicionando"
            @keyup.enter="submitEdit"
            @keyup.esc="cancelEdit"
            @click:append="cancelEdit"
            @click:clear="submitEdit">
            <template v-slot:append>
                <v-tooltip top>
                    <template v-slot:activator="{ on }">
                        <v-btn v-on="on" icon>
                            <v-icon small class="primary--text" @click="submitEdit">fas fa-check</v-icon>
                        </v-btn>
                    </template>
                    {{labelBtnSave}}
                </v-tooltip>
                <v-tooltip top>
                    <template v-slot:activator="{ on }">
                        <v-btn v-on="on" icon>
                            <v-icon small @click="cancelEdit">fas fa-times</v-icon>
                        </v-btn>
                    </template>
                    {{labelBtnCancel}}
                </v-tooltip>
            </template>
        </v-text-field>
    </div>
</template>

<script>
    export default {
        inject:['$validator'],
        name: 'AzTextViewEdit',
        props: {
            value: {
                required: true
            },
            name: {
                required: true,
                type: String
            },
            label: {
                type: String,
                default: ''
            },
            labelBtnSave: {
                type: String,
                default: 'Salvar'
            },
            labelBtnCancel: {
                type: String,
                default: 'Cancelar'
            },
            labelBtnEdit: {
                type: String,
                default: 'Editar'
            },
            maxlength: {
                default: 255
            },
            validate: {
                type: String
            },
            counter: {
                type: Number
            },
            placeholder: {
                type: String
            },
            mask: {
                type: String,
                default: ''
            },
            estaAdicionando: {
                type: Boolean,
                default: false
            },
            disabled: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            model: undefined,
            editing: false
        }),
        watch: {
            value: {
                handler(val) {
                    this.model = val
                },
                immediate: true
            }
        },
        methods: {
            startEdit() {
                if(this.estaAdicionando){
                    return this.$emit('estaAdicionando')
                }
                this.editing = true
                return this.$emit('setaEditando',this.name)
            },
            async submitEdit() {
                if(await this.validarDadosFormulario()){
                    this.editing = false
                    this.$emit('retiraEditando',this.name)
                    this.$emit('input', this.model)
                }
            },
            cancelEdit() {
                this.editing = false
                this.$emit('retiraEditando',this.name)
                this.model = this.value
            },
            async validarDadosFormulario() {
                const estaValidado = await this.$validator._base.validateAll()
                return estaValidado
            },
        }
    }
</script>

<style lang="stylus">
    .az-text-edit
        label
            top -3px
            position relative

        .v-text-field input
            padding 8px 0 0

        .start-edit-btn
            opacity .4

            &:hover
                opacity 1

            .v-btn__content
                top -3px
</style>
