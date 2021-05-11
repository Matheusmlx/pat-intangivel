import {mount} from '@vue/test-utils'
import AzTextViewEditDate from './AzTextViewEditDate'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('AzTextViewEditDate', () => {
    let wrapper, localVue, $validator, errors, vuetify

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()
        errors = {
            collect: jest.fn()
        }
        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            }
        }

    })

    describe('watch', () => {
        it('Deve setar o valor recebido do value no model sempre que tiver alteração', async () => {
            wrapper = mount(AzTextViewEditDate, {
                localVue,
                mocks: {
                    $validator,
                    errors
                }
            })
            await flushPromises()
            wrapper.vm.value = new Date()

            await flushPromises()
            expect(wrapper.vm.model).toEqual(wrapper.vm.$props.value)
        })

        it('Deve chamar o metódo startEdit se o valor recebido por editar for verdadeiro', async () => {
            wrapper = mount(AzTextViewEditDate, {
                localVue,
                vuetify,
                mocks: {
                    $validator,
                    errors
                }
            })
            await flushPromises()
            wrapper.vm.editar = true

            await flushPromises()
            expect(wrapper.emitted().verificaObrigatoriedade).toBeTruthy()
        })

        it('Deve chamar o metódo cancelEdit se o valor recebido por editar for vazio', async () => {
            wrapper = mount(AzTextViewEditDate, {
                localVue,
                vuetify,
                mocks: {
                    $validator,
                    errors
                }
            })
            await flushPromises()
            wrapper.vm.editar = ''

            await flushPromises()
            expect(wrapper.emitted().retiraEditando).toBeTruthy()
        })
    })

    describe('Methods', () => {
        it('Deve emitir um evento de estar editando um valor', async () => {
            wrapper = mount(AzTextViewEditDate, {
                localVue,
                mocks: {
                    $validator,
                    errors
                }
            })
            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.emitted().verificaObrigatoriedade).toBeTruthy()
            expect(wrapper.emitted().setaEditando).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(true)
        })

        it('Deve emitir um evento de estar adicionando um valor', async () => {
            wrapper = mount(AzTextViewEditDate, {
                localVue,
                propsData: {
                    estaAdicionando: true
                },
                mocks: {
                    $validator,
                    errors
                }
            })
            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.emitted().verificaObrigatoriedade).toBeTruthy()
            expect(wrapper.emitted().estaAdicionando).toBeTruthy()
        })

        it('Deve emitir um evento quando estiver retirando a edição', async () => {
            wrapper = mount(AzTextViewEditDate, {
                localVue,
                mocks: {
                    $validator,
                    errors
                }
            })
            wrapper.vm.cancelEdit()
            await flushPromises()
            expect(wrapper.emitted().retiraEditando).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(false)
        })

        it('Deve emitir "retiraEditando" e "input" para o componente pai se os campos for validos', async () => {
            wrapper = mount(AzTextViewEditDate, {
                localVue,
                mocks: {
                    $validator,
                    errors
                }
            })
            wrapper.vm.submitEdit()

            await flushPromises()
            expect(wrapper.vm.editing).toEqual(false)
            expect(wrapper.emitted().retiraEditando).toBeTruthy()
            expect(wrapper.emitted().retiraEditando[0]).toEqual([wrapper.vm.$props.name])
            expect(wrapper.emitted().input).toBeTruthy()
            expect(wrapper.emitted().input[0]).toEqual([wrapper.vm.$data.model])
        })

        it('Não deve emitir "retiraEditando" e "input" para o componente pai se os campos não for validos', async () => {
            $validator = {
                _base: {
                    validateAll: jest.fn().mockReturnValue(false)
                }
            }
            wrapper = mount(AzTextViewEditDate, {
                localVue,
                mocks: {
                    $validator,
                    errors
                }
            })
            wrapper.vm.submitEdit()

            await flushPromises()
            expect(wrapper.emitted().retiraEditando).toBeFalsy()
            expect(wrapper.emitted().input).toBeFalsy()
        })
    })
})
