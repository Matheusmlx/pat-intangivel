<template>
    <div class="az-text az-text-edit az-text-view-edit-date" @keyup.enter="submitEdit">
        <div v-if="!editing">
            <label>{{ label }}</label>
            <p>
                <span v-if="value">{{ $options.filters.azDate(value) }}</span>
                <span v-else> -</span>

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

        <az-date
            v-if="editing"
            v-model="model"
            :label="label"
            date
            :name-date="name"
            :is-required="required"
            v-mask="mask"
            :max-date="maxDate"
            :min-date="minDate"
            :estaAdicionando="estaAdicionando"
            @input="$emit('verificaObrigatoriedade')">
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
        </az-date>
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
            mask: {
                type: String,
                default: '##/##/####'
            },
            minDate: {
                type: String
            },
            maxDate: {
                type: String
            },
            estaAdicionando: {
                type: Boolean,
                default: false
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
            editar(val) {
                if (val) {
                    this.startEdit()
                } else {
                    this.cancelEdit()
                }
            }
        },
        methods: {
            startEdit() {
                this.$emit('verificaObrigatoriedade')
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
                }
            },
            async validarDadosFormulario() {
                const estaValidado = await this.$validator._base.validateAll()
                return estaValidado
            },
            cancelEdit() {
                this.editing = false
                this.$emit('retiraEditando', this.name)
                this.model = this.value
            }
        }
    }
</script>

<style lang="stylus">
</style>
