<template>
    <div class="az-text az-text-edit az-text-view-edit-auto-select">
        <div v-if="!editing" :class="{autoCompleteFornecedorTexto : textoAzTextView}">
            <label>{{ label }}</label>
            <span v-if="model">{{model | azEnum(items)}}</span>
            <span v-else> - </span>
            <v-tooltip top>
                <template v-slot:activator="{ on }">
                    <v-btn v-on="on" icon class="start-edit-btn">
                        <v-icon x-small @click="startEdit">fas fa-edit</v-icon>
                    </v-btn>
                </template>
                {{labelBtnEdit}}
            </v-tooltip>
        </div>
        <az-combo-enum
            v-model="model"
            v-if="editing"
            :label="label"
            :enum-object="items"
            :insertNullItem="false"
            is-required
            :name="name"
            :placeholder="placeholder"
            @input="submitEdit($event)"
            @keyup.esc="cancelEdit"
            @click:append="cancelEdit"
            @click:clear="submitEdit">
            <template v-slot:label v-if="this.$slots['label']">
                <slot name="label"/>
            </template>
            <template v-slot:append>
                <v-tooltip top>
                    <template v-slot:activator="{ on }">
                        <v-btn v-on="on" icon>
                            <v-icon small @click="cancelEdit">fas fa-times</v-icon>
                        </v-btn>
                    </template>
                    {{labelBtnCancel}}
                </v-tooltip>
            </template>
        </az-combo-enum>
    </div>
</template>

<script>
    export default {
        name: 'AzTextViewEditSelect',
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
            },
        },
        methods: {
            startEdit() {
                this.editing = true
            },
            submitEdit(value) {
                this.editing = false
                this.$emit('change', value)
            },
            cancelEdit() {
                this.editing = false
                this.model = this.value
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
