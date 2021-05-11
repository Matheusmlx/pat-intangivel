<template>
    <div class="az-text az-text-edit az-text-view-edit-money">
        <div v-if="!editing">
            <label>{{ label }}</label>
            <p>
                <span v-if="value">{{ value | azCurrency }}</span>
                <span v-else> - </span>
                <v-tooltip top v-if="!disabled">
                    <template v-slot:activator="{ on }">
                        <v-btn v-on="on" icon class="start-edit-btn">
                            <v-icon class="mt-1" x-small @click="startEdit">fas fa-edit</v-icon>
                        </v-btn>
                    </template>
                    {{ labelBtnEdit }}
                </v-tooltip>
            </p>
        </div>

        <az-money
            v-if="editing"
            v-model="model"
            :label="label"
            :name="name"
            :required="required"
            :validation-field="validationField"
            :requerid-message="requiredMessage"
            :placeholder="placeholder"
            :estaAdicionando="estaAdicionando"
            @keyupEnter="submitEdit"
            @input="anulaSeValorZero"
            @keyupEsc="cancelEdit">
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
        </az-money>
    </div>
</template>

<script>
    export default {
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
            required: {
                type: Boolean
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
            validationField: {
                type: Number
            },
            requiredMessage: {
                type: String,
                default: 'O campo é obrigatório'
            },
            editar: {
                type: Boolean
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
            editing: false
        }),
        watch: {
            value: {
                handler(val) {
                    this.model = val
                },
                immediate: true
            },
            async editar(val) {
                if (val) {
                    this.startEdit()
                    this.required = true
                    this.validationField++
                } else {
                    this.required = false
                    await this.$validator._base.validateAll()
                    this.cancelEdit()
                }
            }
        },
        methods: {
            anulaSeValorZero() {
                this.$emit('verificaObrigatoriedade')
                if (this.model === 0) {
                    this.model = null
                }
            },
            startEdit() {
                if (this.estaAdicionando) {
                    return this.$emit('estaAdicionando')
                }
                this.editing = true
                return this.$emit('setaEditando', this.name)
            },
            async submitEdit() {
                if (await this.validarDadosFormulario()) {
                    this.editing = false
                    this.$emit('retiraEditando', this.name)
                    this.$emit('input', this.model)
                    this.$emit('change')
                }
            },
            async validarDadosFormulario() {
                const estaValidado = await this.$validator._base.validateAll()
                return estaValidado && (!this.required || this.model)
            },
            cancelEdit() {
                this.editing = false
                this.$emit('retiraEditando', this.name)
                this.model = this.value
            },
        }
    }
</script>

<style lang="stylus">
</style>
