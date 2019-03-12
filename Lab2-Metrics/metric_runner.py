from nlgeval import NLGEval


def main():

    #Create NLG object to load models
    nlgeval = NLGEval()

    #Read through merged caption file, tab separated
    #First element is image ID, second element reference caption, third+ elements are hypothesis captions

    with open("merged_captions.txt", 'r') as file, open("metrics.txt", "w") as metrics_file:
        for line in file:
            splitLine = line.split("\t")
            id = splitLine[0]
            reference_caption = [splitLine[1]]
            hypothesis_captions = list(filter(None, splitLine[2:]))

            metrics_file.write(id)
            for caption in hypothesis_captions:
                metrics_dict = nlgeval.compute_individual_metrics(ref=reference_caption, hyp=caption)
                for key, value in metrics_dict.items():
                    string = "\t" + key + ": " + str(value)
                    metrics_file.write(string)
                    print(string)

            metrics_file.write("\n")

if __name__ == "__main__":
    main()