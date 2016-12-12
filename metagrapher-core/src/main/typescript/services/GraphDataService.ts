import {INode} from "../data/INode";
import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {Observable, Subject, ReplaySubject} from "rxjs";
import {IGraphConfig} from "../data/IGraphConfig";

@Injectable()
export class GraphDataService {

    private _nodes: Subject<INode[]> = new ReplaySubject(1);

    private _nodesUrl: Subject<string> = new ReplaySubject(1);

    private _styles: Subject<string> = new ReplaySubject(1);

    private _stylesUrl: Subject<string> = new ReplaySubject(1);

    private _config: Subject<IGraphConfig> = new ReplaySubject(1);

    private _configUrl: Subject<string> = new ReplaySubject(1);

    private _configProvider: () => IGraphConfig = () => null;


    constructor(private http: Http) {
        console.log("GraphDataService -- construtor");
        this._nodes.subscribe(nodes => console.log("GraphDataService - nodes", {nodes}));
        this._styles.subscribe(styles => console.log("GraphDataService - styles", {styles}));
        this._config.subscribe(layout => console.log("GraphDataService - config", {layout}));
        this._configUrl.subscribe(configUrl => console.log("GraphDataService - configUrl", {configUrl}));
        this._config.next(null);

        this._nodesUrl
            .do(url => console.log("GraphDataService -- graphRestUrl", {url}))
            .distinctUntilChanged()
            .do(url => console.log("GraphDataService -- graphRestUrl -> http.get", {url}))
            .flatMap(url => this.http.get(url))
            .do(response => console.log("GraphDataService -- graphRestUrl -> http.get -> response", {response}))
            .map((result: Response) => <INode[]> result.json().elements)
            .do(nodes => console.log("GraphDataService -- graphRestUrl -> http.get -> response -> nodes", {nodes}))
            .subscribe(this._nodes);


        this._stylesUrl
            .do(url => console.log("GraphDataService -- graphStylesUrl", {url}))
            .distinctUntilChanged()
            .do(url => console.log("GraphDataService -- graphStylesUrl -> http.get", {url}))
            .flatMap(url => this.http.get(url))
            .do(response => console.log("GraphDataService -- graphStylesUrl -> http.get -> response", {response}))
            .map((result: Response) => <string> result.text())
            .do(css => console.log("GraphDataService -- url -> http.get -> response -> css", {css}))
            .subscribe(this._styles);

        this._configUrl
            .filter(url => !!url)
            .take(1)
            .do(url => console.log("GraphDataService -- load layout", {url}))
            .subscribe(url => this.loadConfigFromUrl(url));
    }


    loadConfig(): void {
        this._configUrl
            .take(1)
            .subscribe(this.loadConfigFromUrl.bind(this));

    }

    storeConfig(): void {
        this._configUrl
            .defaultIfEmpty(null)
            .take(1)
            .subscribe(url => {
                    let config = this._configProvider();
                    console.log("GraphDataService storeConfig", {config, url});
                    let configJson = config;
                    console.log("GraphDataService storeConfig json", {config, url, configJson});
                    if (url && config) {
                        console.log("GraphDataService storeConfig - post", {config, url});
                        this.http.post(url, configJson).subscribe(
                            result =>
                                console.log("GraphDataService storeConfig - post - successful", {configJson, url, result}),
                            error =>
                                console.log("GraphDataService storeConfig - post - error", {configJson, url, error})
                        );
                    } else {
                        console.log("GraphDataService storeConfig - ignoring", {configJson, url});
                    }
                }
            );
    }

    private loadConfigFromUrl(url: string): void {
        this.http.get(url)
            .do(response => console.log("GraphDataService -- configUrl -> http.get -> response", {response}))
            .map((result: Response) => <IGraphConfig> result.json())
            .do(config => console.log("GraphDataService -- configUrl -> http.get -> response -> config", {config}))
            .subscribe(config => this._config.next(config))
    }

    autoConfig(): void {
        this._config.next(null);
    }


    get nodes(): Observable<INode[]> {
        return this._nodes
    }


    get styles(): Observable<String> {
        return this._styles;
    }

    get config(): Observable<IGraphConfig> {
        return this._config;
    }

    set styleUrl(value: string) {
        this._stylesUrl.next(value);
    }

    set nodesUrl(value: string) {
        this._nodesUrl.next(value);
    }

    set configUrl(value: string) {
        this._configUrl.next(value);
    }

    set configProvider(configProvider: () => IGraphConfig) {
        this._configProvider = configProvider;
    }


}