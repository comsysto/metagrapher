export interface IInstance{
    id: string
    hostName: string
    port: number
    homePage: string
    state: string
    links:{[name: string]: string}
}