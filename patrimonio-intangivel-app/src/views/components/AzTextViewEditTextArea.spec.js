import {mount} from '@vue/test-utils'
import AzTextViewEditTextArea from './AzTextViewEditTextArea'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('AzTextViewEditTextArea',()=>{
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
        it('Deve emitir um evento de estar adicionando um valor',async()=>{
            wrapper = mount(AzTextViewEditTextArea,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.startEdit()
            await flushPromises()
            expect(wrapper.vm.editing).toEqual(true)
        })
        it('Deve emitir um evento quando estiver retirando a edição',async()=>{
            wrapper = mount(AzTextViewEditTextArea,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.cancelEdit()
            await flushPromises()
            expect(wrapper.vm.editing).toEqual(false)
        })
    })
})
