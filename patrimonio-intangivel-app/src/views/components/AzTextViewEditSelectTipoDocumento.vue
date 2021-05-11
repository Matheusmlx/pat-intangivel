<template>
    <div class="az-text az-text-edit az-text-view-edit-auto-complete alinhaMentoBox">
        <div v-if="!editing" :class="{autoCompleteFornecedorTexto : textoAzTextView}">
            <label>{{ label }}</label>
            <span class="formatoTexto" v-if="model">{{ setarTipoDocumento().descricao }}</span>
            <span v-else> - </span>
            <v-tooltip top v-if="!disabled">
                <template v-slot:activator="{ on }">
                    <v-btn v-on="on" icon class="start-edit-btn">
                        <v-icon class="mt-1" x-small @click="startEdit">fas fa-edit</v-icon>
                    </v-btn>
                </template>
                {{ labelBtnEdit }}
            </v-tooltip>
        </div>
        <div v-else>
            <v-col cols="2" md="12" sm="12" xs="7" class="py-0 my-0">
                <v-autocomplete
                    v-model="model"
                    :label="label"
                    :items="items"
                    item-value="id"
                    item-text="descricao"
                    no-data-text="Não há tipos com esse nome"
                    :name="name"
                    v-validate="validate"
                    :placeholder="placeholder"
                    :estaAdicionando="estaAdicionando"
                    @keyup.enter="submitEdit"
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
        name: 'AzTextViewEditSelectTipoDocumento',
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
            labelBtnEdit: {
                type: String,
                default: 'Editar'
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
            estaAdicionando: {
                type: Boolean,
                default: false
            },
            disabled: {
                type: Boolean,
                default: false
            }
        },
        $_veeValidate: {
            validator: 'new'
        },
        created() {
            this.setarTipoDocumento()
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
            },
        },
        methods: {
            setarTipoDocumento() {
                return this.items.find(element => element.id === this.model)
            },
            startEdit() {
                if (this.estaAdicionando) {
                    return this.$emit('estaAdicionando')
                }
                this.editing = true
                return this.$emit('setaEditando', this.name)
            },
            submitEdit() {
                this.editing = false
                this.$emit('retiraEditando', this.name)
                this.$emit('input', this.model)
            },
            cancelEdit() {
                this.editing = false
                this.$emit('retiraEditando', this.name)
                this.model = this.value
            },
        }
    }
</script>

<style lang="stylus" scoped>
    .autoCompleteFornecedor
        max-height: 55px;

    .autoCompleteFornecedorTexto
        max-height: 50px;

    .formatoTexto
        color: #6b6b6b

    .alinhaMentoBox
        margin-bottom: 20px
</style>
