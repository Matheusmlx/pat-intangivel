import {mount} from '@vue/test-utils'
import AzTextViewEdit from './AzTextViewEdit'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('AzTextViewEdit',()=>{
    let wrapper,localVue,$validator,errors

    beforeEach(()=>{
        localVue = applicationTestBuilder.build()

        errors = {
            collect:jest.fn()
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


    describe('Methods',()=>{
        it('Deve emitir um evento de estar editando um valor',async()=>{
            wrapper = mount(AzTextViewEdit,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.emitted().setaEditando).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(true)
        })

        it('Deve emitir um evento de estar adicionando um valor', async () => {
            wrapper = mount(AzTextViewEdit, {
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

            expect(wrapper.emitted().estaAdicionando).toBeTruthy()
        })

        it('Deve emitir um evento quando estiver retirando a edição',async()=>{
            wrapper = mount(AzTextViewEdit,{
                localVue,
                mocks:{
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
            wrapper = mount(AzTextViewEdit, {
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
            wrapper = mount(AzTextViewEdit, {
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
