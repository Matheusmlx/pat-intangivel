<template>
    <div class="az-text az-text-edit az-text-view-edit-auto-complete">
        <div v-if="!editing" :class="{autoCompleteFornecedorTexto : textoAzTextView}">
            <label>{{ label }}</label>
            <p>{{ extractItemText() }}
                <v-tooltip top v-if="!disabled">
                    <template v-slot:activator="{ on }">
                        <v-btn v-on="on" icon class="start-edit-btn">
                            <v-icon x-small @click="startEdit">fas fa-edit</v-icon>
                        </v-btn>
                    </template>
                    {{labelBtnEdit}}
                </v-tooltip>
            </p>
        </div>

        <v-autocomplete
            :class="{autoCompleteFornecedor : textoAzTextView}"
            v-if="editing"
            v-model="model"
            :search-input.sync="searchValue"
            :label="label"
            :name="name"
            :items="items"
            :item-text="retornaItemText"
            :item-value="itemValue"
            :no-data-text="noDataText"
            :placeholder="placeholder"
            :error-messages="errors.collect(name)"
            v-validate="validate"
            @keyup.enter="submitEdit"
            @keyup.esc="cancelEdit"
            @click:append="cancelEdit"
            @click:clear="submitEdit">
            <template slot="item" slot-scope="data">
                <span style="max-width: 330px" class="text-truncate">{{ formataCnpj(data.item.cpfCnpj) }} - {{ data.item.nome }}</span>
            </template>
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
        </v-autocomplete>
        <az-text-view
            v-if="textoAzTextView"
            :text="textoAzTextView"/>
    </div>
</template>

<script>
    export default {
        name: 'AzTextViewEdit',
        props: {
            value: {
                required: true
            },
            textoAzTextView: {
                type: String
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
            cnpjPesquisadoContemMascara: {
                type: Boolean,
                required: true,
                default: false
            },
            fornecedorSelecionado: {
                type: Boolean,
                required: true,
                default: true
            },
            eUmNome: {
                type: Boolean,
                required: true,
                default: false
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
            searchValue: undefined,
            itemText: '-',
            salvo:false
        }),
        watch: {
            value: {
                handler(val) {
                    this.model = val
                },
                immediate: true
            },
            async searchValue(val) {
                if (this.search) {
                    await this.search(val)
                }
            }
        },
        methods: {
            startEdit() {
                this.editing = true
            },
            async submitEdit() {
                if (await this.validarDadosFormulario()) {
                    this.editing = false
                    this.salvo = true
                    this.$emit('input', this.model)
                }
            },
            cancelEdit() {
                this.editing = false
                this.salvo = false
                this.model = this.value
            },
            extractItemText() {
                if (this.items && this.items.length > 0) {
                    const found = this.items.filter(forn => forn[this.itemValue] === this.value)
                    if (found.length > 0) {
                        this.itemText = this.formataCnpj(found[0].cpfCnpj) + ' - ' + found[0].nome
                        return this.itemText
                    }

                }
                if(!this.salvo){
                    return this.itemText
                }
                return '-'
            },
            async validarDadosFormulario() {
                const estaValidado = await this.$validator._base.validateAll()
                return estaValidado
            },
            retornaItemText(item) {
                if (this.cnpjPesquisadoContemMascara || this.fornecedorSelecionado) {
                    return `${this.formataCnpj(item.cpfCnpj)}`
                }
                if (this.eUmNome) {
                    return `${item.nome}`
                }
                return `${item.cpfCnpj}`
            },
            formataCnpj(v) {
                v = v.replace(/\D/g, '')
                v = v.replace(/^(\d{2})(\d)/, '$1.$2')
                v = v.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
                v = v.replace(/\.(\d{3})(\d)/, '.$1/$2')
                v = v.replace(/(\d{4})(\d)/, '$1-$2')
                return v
            },
        }
    }
</script>

<style lang="stylus" scoped>
    .autoCompleteFornecedor {
        max-height: 55px;
    }

    .autoCompleteFornecedorTexto {
        max-height: 50px;
    }
</style>
