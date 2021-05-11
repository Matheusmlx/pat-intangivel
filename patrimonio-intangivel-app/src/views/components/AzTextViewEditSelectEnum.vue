<template>
    <div class="az-text az-text-edit">
        <div v-if="!editing">
            <label>{{ label }}</label>
            <v-card class="elevation-0">
                <v-card-text class="pa-0">
                    <p class="d-inline">{{ value ? formatarTexto() : '-' }}
                        <v-tooltip top v-if="!disabled">
                            <template v-slot:activator="{ on }">
                                <v-btn v-on="on" icon class="start-edit-btn">
                                    <v-icon class="mt-1" x-small @click="startEdit">fas fa-edit</v-icon>
                                </v-btn>
                            </template>
                            {{ labelBtnEdit }}
                        </v-tooltip>
                    </p>
                </v-card-text>
            </v-card>
        </div>
        <div v-if="editing">
            <v-col cols="2" md="12" sm="12" xs="7" class="py-0 my-0">
                <v-autocomplete
                    v-model="model"
                    :label="label"
                    :items="items"
                    item-value="id"
                    item-text="descricao"
                    :no-data-text="noDatatext"
                    :name="name"
                    v-validate="validate"
                    :placeholder="placeholder"
                    :error-messages="errors.collect(name)"
                    @keyup.enter="submitEdit"
                    return-object
                    @keyup.esc="cancelEdit">
                    <template v-slot:label v-if="this.$slots['label']">
                        <slot name="label"/>
                    </template>
                    <template v-slot:append>
                        <v-tooltip top>
                            <template v-slot:activator="{ on }">
                                <v-btn v-on="on" icon>
                                    <v-icon small class="primary--text" @click="submitEdit">fas fa-check</v-icon>
                                </v-btn>
                            </template>
                            {{ labelBtnSave }}
                        </v-tooltip>
                        <v-tooltip top>
                            <template v-slot:activator="{ on }">
                                <v-btn v-on="on" icon>
                                    <v-icon small @click="cancelEdit">fas fa-times</v-icon>
                                </v-btn>
                            </template>
                            {{ labelBtnCancel }}
                        </v-tooltip>
                    </template>
                </v-autocomplete>
            </v-col>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'AzTextViewEditSelectSetor',
        props: {
            value: {
                required: true
            },
            textoAzTextView: {
                type: String
            },
            iconClose: {
                type: Boolean,
                default: false
            },
            name: {
                required: true,
                type: String
            },
            items: {
                required: true,
                type: Array,
                default: () => {
                    return []
                }
            },
            noDatatext:{
                type: String,
                default: 'Não há dados com esse nome'
            },
            label: {
                type: String,
                default: ''
            },
            labelBtnCancel: {
                type: String,
                default: 'Cancelar'
            },
            labelBtnSave: {
                type: String,
                default: 'Salvar'
            },
            maxlength: {
                type: Number,
                default: 255
            },
            validate: {
                type: String
            },
            placeholder: {
                type: String
            },
            returnObject: {
                type: Boolean,
                default: true
            },
            labelBtnEdit: {
                type: String,
                default: 'Editar'
            },
            itemText: {
                type: String
            },
            itemValue: {
                type: String
            },
            formatValue: {
                type: Function
            },
            noDataText: {
                type: String
            },
            search: {
                type: Function
            },
            disabled: {
                type: Boolean,
                default: false
            }
        },
        $_veeValidate: {
            validator: 'new'
        },
        data: () => ({
            model: undefined,
            editing: false,
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
                this.editing = true
                this.$emit('setaEditando', this.name)
            },
            async submitEdit() {
                if(await this.validarDadosFormulario()){
                    this.value = this.model
                    this.editing = false
                    this.$emit('retiraEditando', this.name)
                    this.$emit('input', this.model)
                }
            },
            cancelEdit() {
               this.editing = false
                this.$emit('retiraEditando',this.name)
                this.model = this.value
            },
            formatarTexto() {
                return `${this.model.sigla} - ${this.model.nome}`
            },
            async validarDadosFormulario() {
                const estaValidado = await this.$validator._base.validateAll()
                return estaValidado
            }
        }
    }
</script>

<style lang="stylus" scoped>
.autoCompleteFornecedor
    max-height: 55px;

.autoCompleteSetorTexto
    max-height: 50px;

.formatoTexto
    color: #6b6b6b

.alinhaMentoBox
    margin-bottom: 20px
</style>
