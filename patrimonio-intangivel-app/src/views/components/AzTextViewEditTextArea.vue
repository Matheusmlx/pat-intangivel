<template>
    <div class="az-text az-text-edit az-text-view-edit-text-area">
        <div v-if="!editing">
            <label>{{ label }}</label>
            <v-card class="elevation-0">
                <v-card-text class="pa-0">
                    <p class="text-justify">
                        {{ value }}
                        <v-tooltip top v-if="!disabled">
                            <template v-slot:activator="{ on }">
                                <v-btn v-on="on" icon class="start-edit-btn">
                                    <v-icon x-small @click="startEdit">fas fa-edit</v-icon>
                                </v-btn>
                            </template>
                            {{labelBtnEdit}}
                        </v-tooltip>
                    </p>
                </v-card-text>
            </v-card>
        </div>

        <v-textarea
            v-if="editing"
            v-model="model"
            :label="label"
            :name="name"
            :maxlength="maxlength"
            :error-messages="errors.collect(name)"
            v-validate="validate"
            :counter="counter"
            :placeholder="placeholder"
            auto-grow
            rows="1"
            @keyupEsc="cancelEdit"
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
        </v-textarea>
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
                required: true,
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
                type: Number,
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
            value(val) {
                this.model = val
            }
        },
        methods: {
            startEdit() {
                this.editing = true
            },
            async submitEdit() {
                if (await this.validarDadosFormulario()) {
                    this.editing = false
                    this.$emit('input', this.model.trim())
                }
            },
            async validarDadosFormulario() {
                const estaValidado = await this.$validator._base.validateAll()
                return estaValidado
            },
            cancelEdit() {
                this.editing = false
                this.model = this.value
            }
        }
    }
</script>

<style lang="stylus">
</style>
